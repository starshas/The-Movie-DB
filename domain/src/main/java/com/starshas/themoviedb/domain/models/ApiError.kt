package com.starshas.themoviedb.domain.models

sealed class DomainApiError : Exception() {
    object NetworkError : DomainApiError()
    data class HttpError(val code: Int, val errorMessage: String) : DomainApiError()
    data class GenericError(val error: Exception) : DomainApiError()
}
