package com.uniandes.vinilosapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uniandes.vinilosapp.database.VinilosDatabase
import com.uniandes.vinilosapp.models.Album
import com.uniandes.vinilosapp.models.GENRE
import com.uniandes.vinilosapp.models.RECORD_LABEL
import com.uniandes.vinilosapp.repositories.AlbumRepository
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateAlbumViewModel(application: Application) : AndroidViewModel(application) {

    private val albumsRepository =
            AlbumRepository(
                    application,
                    VinilosDatabase.getDatabase(application.applicationContext).albumsDao()
            )

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?>
        get() = _error

    private val _createSuccess = MutableLiveData<Boolean>(false)
    val createSuccess: LiveData<Boolean>
        get() = _createSuccess

    // Field touch states - track if user has interacted with the field
    private val _nameTouched = MutableLiveData<Boolean>(false)
    val nameTouched: LiveData<Boolean>
        get() = _nameTouched

    private val _descriptionTouched = MutableLiveData<Boolean>(false)
    val descriptionTouched: LiveData<Boolean>
        get() = _descriptionTouched

    private val _coverUrlTouched = MutableLiveData<Boolean>(false)
    val coverUrlTouched: LiveData<Boolean>
        get() = _coverUrlTouched

    private val _genreTouched = MutableLiveData<Boolean>(false)
    val genreTouched: LiveData<Boolean>
        get() = _genreTouched

    private val _recordLabelTouched = MutableLiveData<Boolean>(false)
    val recordLabelTouched: LiveData<Boolean>
        get() = _recordLabelTouched

    private val _releaseDateTouched = MutableLiveData<Boolean>(false)
    val releaseDateTouched: LiveData<Boolean>
        get() = _releaseDateTouched

    // Validation states
    private val _nameError = MutableLiveData<String?>(null)
    val nameError: LiveData<String?>
        get() = _nameError

    private val _descriptionError = MutableLiveData<String?>(null)
    val descriptionError: LiveData<String?>
        get() = _descriptionError

    private val _coverUrlError = MutableLiveData<String?>(null)
    val coverUrlError: LiveData<String?>
        get() = _coverUrlError

    private val _genreError = MutableLiveData<String?>(null)
    val genreError: LiveData<String?>
        get() = _genreError

    private val _recordLabelError = MutableLiveData<String?>(null)
    val recordLabelError: LiveData<String?>
        get() = _recordLabelError

    private val _releaseDateError = MutableLiveData<String?>(null)
    val releaseDateError: LiveData<String?>
        get() = _releaseDateError

    private val _isFormValid = MutableLiveData<Boolean>(false)
    val isFormValid: LiveData<Boolean>
        get() = _isFormValid

    private val _isValidUrl = MutableLiveData<Boolean>(false)
    val isValidUrl: LiveData<Boolean>
        get() = _isValidUrl

    // Form fields
    private val _name = MutableLiveData<String>("")
    val name: LiveData<String>
        get() = _name

    private val _description = MutableLiveData<String>("")
    val description: LiveData<String>
        get() = _description

    private val _coverUrl = MutableLiveData<String>("")
    val coverUrl: LiveData<String>
        get() = _coverUrl

    private val _genre = MutableLiveData<GENRE?>(null)
    val genre: LiveData<GENRE?>
        get() = _genre

    private val _recordLabel = MutableLiveData<RECORD_LABEL?>(null)
    val recordLabel: LiveData<RECORD_LABEL?>
        get() = _recordLabel

    private val _releaseDate = MutableLiveData<Date?>(null)
    val releaseDate: LiveData<Date?>
        get() = _releaseDate

    // Formatted display date for UI
    private val _releaseDateFormatted = MutableLiveData<String>("Seleccionar fecha")
    val releaseDateFormatted: LiveData<String>
        get() = _releaseDateFormatted

    // Functions to update form fields with validation
    fun updateName(value: String) {
        _name.value = value
        if (!_nameTouched.value!!) {
            _nameTouched.value = true
        }
        validateName(value)
        validateForm()
    }

    fun updateDescription(value: String) {
        _description.value = value
        if (!_descriptionTouched.value!!) {
            _descriptionTouched.value = true
        }
        validateDescription(value)
        validateForm()
    }

    fun updateCoverUrl(value: String) {
        _coverUrl.value = value
        if (!_coverUrlTouched.value!!) {
            _coverUrlTouched.value = true
        }
        val urlIsValid = isValidUrl(value)
        _isValidUrl.value = urlIsValid
        validateCoverUrl(value, urlIsValid)
        validateForm()
    }

    fun updateGenre(value: GENRE?) {
        _genre.value = value
        if (!_genreTouched.value!!) {
            _genreTouched.value = true
        }
        validateGenre(value)
        validateForm()
    }

    fun updateRecordLabel(value: RECORD_LABEL?) {
        _recordLabel.value = value
        if (!_recordLabelTouched.value!!) {
            _recordLabelTouched.value = true
        }
        validateRecordLabel(value)
        validateForm()
    }

    fun updateReleaseDate(date: Date?) {
        _releaseDate.value = date
        if (!_releaseDateTouched.value!!) {
            _releaseDateTouched.value = true
        }

        if (date != null) {
            // Format for display
            val displayFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("es", "ES"))
            _releaseDateFormatted.value = displayFormat.format(date)
        } else {
            _releaseDateFormatted.value = "Seleccionar fecha"
        }

        validateReleaseDate(date)
        validateForm()
    }

    // Functions to mark fields as touched
    fun onNameTouched() {
        if (!_nameTouched.value!!) {
            _nameTouched.value = true
            validateName(_name.value)
            validateForm()
        }
    }

    fun onDescriptionTouched() {
        if (!_descriptionTouched.value!!) {
            _descriptionTouched.value = true
            validateDescription(_description.value)
            validateForm()
        }
    }

    fun onCoverUrlTouched() {
        if (!_coverUrlTouched.value!!) {
            _coverUrlTouched.value = true
            validateCoverUrl(_coverUrl.value, isValidUrl(_coverUrl.value!!))
            validateForm()
        }
    }

    fun onGenreTouched() {
        if (!_genreTouched.value!!) {
            _genreTouched.value = true
            validateGenre(_genre.value)
            validateForm()
        }
    }

    fun onRecordLabelTouched() {
        if (!_recordLabelTouched.value!!) {
            _recordLabelTouched.value = true
            validateRecordLabel(_recordLabel.value)
            validateForm()
        }
    }

    fun onReleaseDateTouched() {
        if (!_releaseDateTouched.value!!) {
            _releaseDateTouched.value = true
            validateReleaseDate(_releaseDate.value)
            validateForm()
        }
    }

    // Function to check if a string is a valid URL
    private fun isValidUrl(url: String?): Boolean {
        return try {
            if (url.isNullOrBlank()) return false
            URL(url)
            true
        } catch (e: MalformedURLException) {
            false
        }
    }

    private fun validateName(value: String?) {
        _nameError.value =
                if (value.isNullOrBlank()) {
                    "El nombre del álbum es obligatorio"
                } else {
                    null
                }
    }

    private fun validateDescription(value: String?) {
        _descriptionError.value =
                if (value.isNullOrBlank()) {
                    "La descripción es obligatoria"
                } else {
                    null
                }
    }

    private fun validateCoverUrl(value: String?, isValid: Boolean) {
        _coverUrlError.value =
                when {
                    value.isNullOrBlank() -> "La URL de portada es obligatoria"
                    !isValid -> "La URL ingresada no es válida"
                    else -> null
                }
    }

    private fun validateGenre(value: GENRE?) {
        _genreError.value =
                if (value == null) {
                    "Debes seleccionar un género"
                } else {
                    null
                }
    }

    private fun validateRecordLabel(value: RECORD_LABEL?) {
        _recordLabelError.value =
                if (value == null) {
                    "Debes seleccionar una disquera"
                } else {
                    null
                }
    }

    private fun validateReleaseDate(date: Date?) {
        _releaseDateError.value =
                when {
                    date == null -> "La fecha de lanzamiento es obligatoria"
                    isDateInFuture(date) -> "La fecha de lanzamiento no puede ser futura"
                    else -> null
                }
    }

    private fun isDateInFuture(date: Date): Boolean {
        val today =
                Calendar.getInstance()
                        .apply {
                            set(Calendar.HOUR_OF_DAY, 0)
                            set(Calendar.MINUTE, 0)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                        .time

        return date.after(today)
    }

    private fun validateForm() {
        // Check if all required fields are filled and valid
        val nameValid = _nameError.value == null && !_name.value.isNullOrBlank()
        val descriptionValid =
                _descriptionError.value == null && !_description.value.isNullOrBlank()
        val coverUrlValid = _coverUrlError.value == null && !_coverUrl.value.isNullOrBlank()
        val genreValid = _genreError.value == null && _genre.value != null
        val recordLabelValid = _recordLabelError.value == null && _recordLabel.value != null
        val releaseDateValid = _releaseDateError.value == null && _releaseDate.value != null

        _isFormValid.value =
                nameValid &&
                        descriptionValid &&
                        coverUrlValid &&
                        genreValid &&
                        recordLabelValid &&
                        releaseDateValid
    }

    fun createAlbum() {
        if (_isFormValid.value != true) return

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                // Create a placeholder album ID (will be replaced by the server)
                val tempAlbumId = 0

                // Format the release date
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val releaseDate = sdf.format(_releaseDate.value!!)

                val album =
                        Album(
                                albumId = tempAlbumId,
                                name = _name.value!!,
                                cover = _coverUrl.value!!,
                                releaseDate = releaseDate,
                                description = _description.value!!,
                                genre = _genre.value!!,
                                recordLabel = _recordLabel.value!!
                        )

                withContext(Dispatchers.IO) { albumsRepository.createAlbum(album) }
                _createSuccess.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Error creating album"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetState() {
        _createSuccess.value = false
        _error.value = null
    }

    init {
        // Initialize touch states to false
        _nameTouched.value = false
        _descriptionTouched.value = false
        _coverUrlTouched.value = false
        _genreTouched.value = false
        _recordLabelTouched.value = false
        _releaseDateTouched.value = false
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CreateAlbumViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST") return CreateAlbumViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
