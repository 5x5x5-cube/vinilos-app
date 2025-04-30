package com.uniandes.vinilosapp.repositories

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.uniandes.vinilosapp.database.AlbumDao
import com.uniandes.vinilosapp.models.Album
import com.uniandes.vinilosapp.models.AlbumDetails
import com.uniandes.vinilosapp.network.NetworkServiceAdapter

class AlbumRepository (val application: Application,  private val albumDao: AlbumDao){


    suspend fun getAlbums(): List<Album> {
        var cached = albumDao.getAlbums()
        return if (cached.isNullOrEmpty()) {
            val cm =
                application.baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_WIFI && cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_MOBILE) {
                emptyList()
            } else NetworkServiceAdapter.getInstance(application).getAlbums()
        } else cached
    }

    suspend fun getAlbum(albumId:Int): AlbumDetails {
        return NetworkServiceAdapter.getInstance(application).getAlbum(albumId)
    }



}