package com.proxymitylab.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.proxymitylab.demo.model.AQIData
import com.proxymitylab.demo.network.CallResponseStatus
import com.proxymitylab.demo.network.SocketUpdate
import com.proxymitylab.demo.network.WebServicesProvider
import com.proxymitylab.demo.repositry.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var interactor: MainInteractor?=null
    val aqiMutableLiveData = MutableLiveData<CallResponseStatus<List<AQIData>>>()
    val gson=GsonBuilder().create()

    init {
        interactor= MainInteractor(MainRepository(WebServicesProvider()))
    }

    fun subscribeToSocketEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                interactor?.startSocket()?.consumeEach {

                    if (it.exception == null) {
                        println("Collecting : ${it.text}")

                        val data= gson.fromJson(it.text,Array<AQIData>::class.java).toList()

                        aqiMutableLiveData.postValue( CallResponseStatus.success(data))
                    } else {
                        onSocketError(it.exception!!)
                    }
                }
            } catch (ex: java.lang.Exception) {
                onSocketError(ex)
            }
        }
    }

    private fun onSocketError(ex: Throwable) {
        aqiMutableLiveData.postValue(CallResponseStatus.error(ex))
    }

   public override fun onCleared() {
        interactor?.stopSocket()
        super.onCleared()
    }

}

class MainInteractor constructor(private val repository: MainRepository) {
    fun stopSocket() {
        repository.closeSocket()
    }
    fun startSocket(): Channel<SocketUpdate> = repository.startSocket()

}

