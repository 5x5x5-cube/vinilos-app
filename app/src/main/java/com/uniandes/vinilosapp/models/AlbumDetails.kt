package com.uniandes.vinilosapp.models

data class AlbumDetails (
    val albumId:Int,
    val name:String,
    val cover:String,
    val releaseDate:String,
    val description:String,
    val genre:String,
    val recordLabel:String,
    val performers: List<Performer>,
    val tracks: List<Track>
)