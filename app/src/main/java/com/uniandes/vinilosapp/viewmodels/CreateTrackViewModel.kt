package com.uniandes.vinilosapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uniandes.vinilosapp.database.VinilosDatabase
import com.uniandes.vinilosapp.repositories.AlbumRepository
import kotlinx.coroutines.launch

class CreateTrackViewModel(application: Application) : AndroidViewModel(application) {

    private val albumsRepository =
            AlbumRepository(
                    application,
                    VinilosDatabase.getDatabase(application.applicationContext).albumsDao()
            )

    // Loading and error states
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?>
        get() = _error

    private val _createSuccess = MutableLiveData<Boolean>(false)
    val createSuccess: LiveData<Boolean>
        get() = _createSuccess

    // Album ID state
    private val _albumId = MutableLiveData<Int?>(null)
    val albumId: LiveData<Int?>
        get() = _albumId

    private val _albumName = MutableLiveData<String>("")
    val albumName: LiveData<String>
        get() = _albumName

    // Field touch states - track if user has interacted with the field
    private val _nameTouched = MutableLiveData<Boolean>(false)
    val nameTouched: LiveData<Boolean>
        get() = _nameTouched

    private val _durationTouched = MutableLiveData<Boolean>(false)
    val durationTouched: LiveData<Boolean>
        get() = _durationTouched

    // Validation states
    private val _nameError = MutableLiveData<String?>(null)
    val nameError: LiveData<String?>
        get() = _nameError

    private val _durationError = MutableLiveData<String?>(null)
    val durationError: LiveData<String?>
        get() = _durationError

    private val _isFormValid = MutableLiveData<Boolean>(false)
    val isFormValid: LiveData<Boolean>
        get() = _isFormValid

    // Form fields
    private val _name = MutableLiveData<String>("")
    val name: LiveData<String>
        get() = _name

    private val _duration = MutableLiveData<String>("")
    val duration: LiveData<String>
        get() = _duration

    // Functions to update form fields with validation
    fun updateName(value: String) {
        _name.value = value
        if (!_nameTouched.value!!) {
            _nameTouched.value = true
        }
        validateName(value)
        validateForm()
    }

    fun updateDuration(value: String) {
        _duration.value = value
        if (!_durationTouched.value!!) {
            _durationTouched.value = true
        }
        validateDuration(value)
        validateForm()
    }

    fun setAlbum(id: Int, name: String) {
        _albumId.value = id
        _albumName.value = name
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

    fun onDurationTouched() {
        if (!_durationTouched.value!!) {
            _durationTouched.value = true
            validateDuration(_duration.value)
            validateForm()
        }
    }

    private fun validateName(value: String?) {
        _nameError.value =
                if (value.isNullOrBlank()) {
                    "El nombre de la canción es obligatorio"
                } else {
                    null
                }
    }

    private fun validateDuration(value: String?) {
        _durationError.value =
                when {
                    value.isNullOrBlank() -> "La duración es obligatoria"
                    !isValidDurationFormat(value) -> "Formato inválido. Use mm:ss (ej. 3:45)"
                    else -> null
                }
    }

    // Validates mm:ss format (allows single digit for minutes)
    private fun isValidDurationFormat(duration: String): Boolean {
        return duration.matches(Regex("^\\d{1,2}:\\d{2}$"))
    }

    private fun validateForm() {
        // Check if all required fields are filled and valid
        val nameValid = _nameError.value == null && !_name.value.isNullOrBlank()
        val durationValid = _durationError.value == null && !_duration.value.isNullOrBlank()
        val albumSelected = _albumId.value != null

        _isFormValid.value = nameValid && durationValid && albumSelected
    }

    fun createTrack() {
        val albumIdValue = _albumId.value
        if (_isFormValid.value != true || albumIdValue == null) return

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                albumsRepository.createTrack(albumIdValue, _name.value!!, _duration.value!!)
                _createSuccess.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al crear la canción"
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
        _durationTouched.value = false
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CreateTrackViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST") return CreateTrackViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
