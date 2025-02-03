package com.starshas.themoviedb.domain.utils

interface StringProvider {
    fun getString(resId: Int): String

    fun getHttpErrorMessage(
        resId: Int,
        httpCode: Int,
        message: String,
    ): String
}
