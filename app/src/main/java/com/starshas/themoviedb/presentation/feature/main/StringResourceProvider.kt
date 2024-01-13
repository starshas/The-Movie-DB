package com.starshas.themoviedb.presentation.feature.main

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StringResourceProvider @Inject constructor(@ApplicationContext private val  context: Context) {
    fun getString(@StringRes resId:Int): String {
        return context.getString(resId)
    }

    fun getHttpErrorMessage(@StringRes resId: Int, httpCode: Int, message: String): String {
        val errorMessageTemplate = context.getString(resId)
        return String.format(errorMessageTemplate, httpCode, message)
    }
}
