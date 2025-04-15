package com.uniandes.vinilosapp.repositories

import android.app.Application
import com.uniandes.vinilosapp.models.Album
import com.uniandes.vinilosapp.models.AlbumDetails
import com.uniandes.vinilosapp.network.NetworkServiceAdapter

class AlbumRepository (val application: Application){
    suspend fun getAlbums(): List<Album> {
        return NetworkServiceAdapter.getInstance(application).getAlbums()
    }

    suspend fun getAlbum(albumId:Int): AlbumDetails {
        return NetworkServiceAdapter.getInstance(application).getAlbum(albumId)
    }



}