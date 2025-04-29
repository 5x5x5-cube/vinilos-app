package com.uniandes.vinilosapp.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.uniandes.vinilosapp.models.Performer
import com.uniandes.vinilosapp.repositories.PerformerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerformerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PerformerRepository(application)

    private val _performers = MutableLiveData<List<Performer>>()
    val performers: LiveData<List<Performer>> get() = _performers

    private val _eventNetworkError = MutableLiveData(false)
    val eventNetworkError: LiveData<Boolean> get() = _eventNetworkError

    private val _isNetworkErrorShown = MutableLiveData(false)
    val isNetworkErrorShown: LiveData<Boolean> get() = _isNetworkErrorShown

    init {
        refreshData()
    }

    private fun refreshData() {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val data = withContext(Dispatchers.IO) { repository.getAllPerformers() }
                _performers.postValue(data)
                _eventNetworkError.postValue(false)
                _isNetworkErrorShown.postValue(false)
            } catch (e: Exception) {
                _eventNetworkError.postValue(true)
            }
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PerformerViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PerformerViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}