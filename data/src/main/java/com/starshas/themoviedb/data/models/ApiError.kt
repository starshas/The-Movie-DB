package com.starshas.themoviedb.data

sealed class ApiError : Exception() {
    object NetworkError : ApiError()
    data class HttpError(val code: Int, val errorMessage: String) : ApiError()
    data class GenericError(val error: Exception) : ApiError()
}
