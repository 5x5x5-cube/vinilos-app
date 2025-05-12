package com.uniandes.vinilosapp.repositories

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.uniandes.vinilosapp.database.AlbumDao
import com.uniandes.vinilosapp.models.Album
import com.uniandes.vinilosapp.models.AlbumDetails
import com.uniandes.vinilosapp.models.Track
import com.uniandes.vinilosapp.network.NetworkServiceAdapter

class AlbumRepository(val application: Application, private val albumDao: AlbumDao) {

    suspend fun getAlbums(): List<Album> {
        var cached = albumDao.getAlbums()
        return if (cached.isNullOrEmpty()) {
            val cm =
                    application.baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as
                            ConnectivityManager
            if (cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_WIFI &&
                            cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_MOBILE
            ) {
                emptyList()
            } else NetworkServiceAdapter.getInstance(application).getAlbums()
        } else cached
    }

    suspend fun getAlbum(albumId: Int): AlbumDetails {
        return NetworkServiceAdapter.getInstance(application).getAlbum(albumId)
    }

    suspend fun createAlbum(album: Album): Album {
        val cm =
                application.baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as
                        ConnectivityManager

        val activeNetwork = cm.activeNetwork
        val capabilities = cm.getNetworkCapabilities(activeNetwork)
        val isConnected =
                capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        if (!isConnected) {
            throw Exception("No internet connection available")
        }

        val createdAlbum = NetworkServiceAdapter.getInstance(application).createAlbum(album)

        // Clear the local cache so that the next getAlbums call will fetch from backend
        albumDao.clearAll()

        return createdAlbum
    }

    suspend fun createTrack(albumId: Int, name: String, duration: String): Track {
        val cm =
                application.baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as
                        ConnectivityManager

        val activeNetwork = cm.activeNetwork
        val capabilities = cm.getNetworkCapabilities(activeNetwork)
        val isConnected =
                capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        if (!isConnected) {
            throw Exception("No internet connection available")
        }

        return NetworkServiceAdapter.getInstance(application).createTrack(albumId, name, duration)
    }
}
