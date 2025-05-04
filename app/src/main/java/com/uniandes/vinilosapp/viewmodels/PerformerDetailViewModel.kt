package com.uniandes.vinilosapp.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.uniandes.vinilosapp.models.Performer
import com.uniandes.vinilosapp.models.PerformerType
import com.uniandes.vinilosapp.repositories.PerformerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerformerDetailViewModel(
        application: Application,
        private val performerId: Int,
        private val performerType: PerformerType? = null
) : AndroidViewModel(application) {

    private val _performer = MutableLiveData<Performer>()
    val performer: LiveData<Performer>
        get() = _performer

    private val _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private val _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private val performerRepository = PerformerRepository(application)

    init {
        refreshData()
    }

    private fun refreshData() {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                withContext(Dispatchers.IO) {
                    val performer = performerRepository.getPerformer(performerId, performerType)
                    _performer.postValue(performer)
                    _eventNetworkError.postValue(false)
                    _isNetworkErrorShown.postValue(false)
                }
            } catch (e: Exception) {
                _eventNetworkError.postValue(true)
            }
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
        refreshData()
    }

    class Factory(
            private val app: Application,
            private val performerId: Int,
            private val performerType: PerformerType? = null
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PerformerDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PerformerDetailViewModel(app, performerId, performerType) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
