package com.mertdev.weighttracking.utils.enums

enum class Status {
    SUCCESS, LOADING, ERROR
}

sealed class DataStatus<T>(
    val status: Status, val data: T? = null, val message: String? = null
) {
    class Loading<T>(data: T? = null) : DataStatus<T>(Status.LOADING, data)
    class Error<T>(message: String?, data: T? = null) : DataStatus<T>(Status.ERROR, data, message)
    class Success<T>(data: T?) : DataStatus<T>(Status.SUCCESS, data)
}