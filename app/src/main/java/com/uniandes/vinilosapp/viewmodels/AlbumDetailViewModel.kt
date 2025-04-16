package com.uniandes.vinilosapp.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.uniandes.vinilosapp.models.AlbumDetails
import com.uniandes.vinilosapp.repositories.AlbumRepository
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumDetailViewModel(application: Application, albumId: Int) : AndroidViewModel(application) {
    private val albumsRepository = AlbumRepository(application)

    private val _albumOriginal = MutableLiveData<AlbumDetails>()

    val album: LiveData<AlbumDetails> =
            _albumOriginal.map { albumDetails ->
                AlbumDetails(
                        albumId = albumDetails.albumId,
                        name = albumDetails.name,
                        cover = albumDetails.cover,
                        releaseDate = formatDate(albumDetails.releaseDate),
                        description = albumDetails.description,
                        genre = albumDetails.genre,
                        recordLabel = albumDetails.recordLabel,
                        performers = albumDetails.performers,
                        tracks = albumDetails.tracks
                )
            }

    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    val id: Int = albumId

    init {
        refreshData()
    }

    private fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date = inputFormat.parse(dateString)

            val outputFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("es", "ES"))

            date?.let { outputFormat.format(it) } ?: dateString
        } catch (e: Exception) {
            dateString
        }
    }

    private fun refreshData() {
        try {
            viewModelScope.launch(Dispatchers.Default) {
                withContext(Dispatchers.IO) {
                    var data = albumsRepository.getAlbum(id)
                    _albumOriginal.postValue(data)
                }
                _eventNetworkError.postValue(false)
                _isNetworkErrorShown.postValue(false)
            }
        } catch (e: Exception) {
            _eventNetworkError.value = true
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application, val albumId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlbumDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST") return AlbumDetailViewModel(app, albumId) as T
            }
            throw IllegalArgumentException("Unable to construct detailsviewmodel")
        }
    }
}
