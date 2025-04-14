package vinillos.data.repository

import vinillos.data.model.Album
import vinillos.data.service.RetrofitClient
import vinillos.data.service.AlbumService

class AlbumRepository {
    private val albumService: AlbumService = RetrofitClient.retrofit.create(AlbumService::class.java)

    suspend fun fetchAlbums(): List<Album> {
        return albumService.getAlbums()
    }
}
