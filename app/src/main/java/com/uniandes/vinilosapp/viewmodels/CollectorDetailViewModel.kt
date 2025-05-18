package com.uniandes.vinilosapp.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.uniandes.vinilosapp.database.VinilosDatabase
import com.uniandes.vinilosapp.models.CollectorDetails
import com.uniandes.vinilosapp.repositories.CollectorRepository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CollectorDetailsViewModel(application: Application, collectorId: Int) : AndroidViewModel(application) {
    private val collectorsRepository = CollectorRepository(application, VinilosDatabase.getDatabase(application.applicationContext).collectorDao())

    private val _collector = MutableLiveData<CollectorDetails>()

    val collector: LiveData<CollectorDetails>
        get() = _collector

    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    val id:Int = collectorId

    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() {
        try {
            viewModelScope.launch(Dispatchers.Default){
                withContext(Dispatchers.IO){
                    var data = collectorsRepository.getCollectorDetail(id)
                    _collector.postValue(data)
                }
                _eventNetworkError.postValue(false)
                _isNetworkErrorShown.postValue(false)
            }
        }
        catch (e:Exception){
            _eventNetworkError.value = true
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application, val collectorId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CollectorDetailsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CollectorDetailsViewModel(app, collectorId) as T
            }
            throw IllegalArgumentException("Unable to construct detailsviewmodel")
        }
    }
}