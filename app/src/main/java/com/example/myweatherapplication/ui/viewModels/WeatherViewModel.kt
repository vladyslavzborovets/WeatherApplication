package com.example.myweatherapplication.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapplication.network.model.WeatherResponse
import com.example.myweatherapplication.network.service.APIService
import com.example.myweatherapplication.utils.Const.Companion.API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * View model class with one function for API calling getWeatherFromAPI()
 */
@HiltViewModel
class WeatherViewModel @Inject constructor(private val retrofitInstance: APIService) : ViewModel() {
    val responseContainer = MutableLiveData<WeatherResponse>()
    val errorMessage = MutableLiveData<String>()
    val isShowProgress = MutableLiveData<Boolean>()
    val isWeatherDataAvailable = MutableLiveData<Boolean>()
    var job: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled : ${ throwable.localizedMessage}")
    }

    fun getWeatherFromAPI(expression: String){
        isShowProgress.value = true
        isWeatherDataAvailable.value = false
        job = viewModelScope.launch(exceptionHandler){
              val response = retrofitInstance.getWeatherByCity(expression, API_KEY)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    responseContainer.postValue(response.body())
                    isShowProgress.value = false
                    isWeatherDataAvailable.value = true
                } else {
                    onError("Error : ${response.message()}")
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        isShowProgress.value = false
    }

    public override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
