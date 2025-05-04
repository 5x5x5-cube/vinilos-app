package com.uniandes.vinilosapp.database


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uniandes.vinilosapp.models.Album

@Dao
interface AlbumDao {
    @Query("SELECT * FROM tb_albums")
    fun getAlbums():List<Album>

    @Query("SELECT * FROM tb_albums WHERE albumId = :idalbum")
    fun getAlbum(idalbum:Int): Album

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(album: Album)
}