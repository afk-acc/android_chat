package com.example.exam_chat.resourse

sealed class Resourse<T>(
    var data: T? = null,
    val message: String = ""
) {
    class Success<T>(data: T) : Resourse<T>(data)
    class Error<T>(data: T?, message: String) : Resourse<T>(data, message)
    class Loading<T>() : Resourse<T>()
}