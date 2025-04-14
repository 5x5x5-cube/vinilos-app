package vinillos.data.service

import vinillos.data.model.Album
import retrofit2.http.GET

interface AlbumService {
    @GET("albums")
    suspend fun getAlbums(): List<Album>
}
