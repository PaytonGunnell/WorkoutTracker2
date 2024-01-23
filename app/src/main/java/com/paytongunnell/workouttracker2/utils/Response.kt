package com.paytongunnell.workouttracker2.utils

sealed class Response<T> {
    class Loading<T>: Response<T>()
    class Success<T>(val data: T): Response<T>()
    class Error<T>(val message: String?, val data: T? = null): Response<T>()
}