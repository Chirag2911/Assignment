package com.proxymitylab.demo.repositry

import com.proxymitylab.demo.network.SocketUpdate
import com.proxymitylab.demo.network.WebServicesProvider
import kotlinx.coroutines.channels.Channel

class MainRepository constructor(private val webServicesProvider: WebServicesProvider) {

    fun startSocket(): Channel<SocketUpdate> =
        webServicesProvider.startSocket()

    fun closeSocket() {
        webServicesProvider.stopSocket()
    }
}