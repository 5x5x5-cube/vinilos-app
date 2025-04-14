package vinillos.ui.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import vinillos.data.repository.AlbumRepository
import vinillos.data.model.Album
import kotlinx.coroutines.launch

class AlbumViewModel(private val albumRepository: AlbumRepository) : ViewModel() {

    val albums = MutableLiveData<List<Album>>()

    fun loadAlbums() {
        viewModelScope.launch {
            val fetchedAlbums   = albumRepository.fetchAlbums()
            albums.value = fetchedAlbums
        }
    }
}
