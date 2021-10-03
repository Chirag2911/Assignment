package com.proxymitylab.demo.network

class CallResponseStatus<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {

        fun <T> success(data: T?): CallResponseStatus<T> {
            return CallResponseStatus(Status.SUCCESS, data, null)
        }

        fun <T> error(t: Throwable): CallResponseStatus<T> {
            return CallResponseStatus(Status.ERROR, null, t.message)
        }
    }

    enum class Status {
        SUCCESS, ERROR
    }
}