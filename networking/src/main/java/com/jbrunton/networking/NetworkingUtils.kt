package com.jbrunton.networking

import retrofit2.HttpException
import com.google.gson.Gson
import com.jbrunton.networking.resources.HttpErrorResponse
import java.lang.NullPointerException


private val gson = Gson()

fun HttpException.parseStatusMessage(): String {
    val errorResponse = gson.fromJson(this.response().errorBody()?.string(), HttpErrorResponse::class.java)
    return errorResponse.statusMessage ?: throw NullPointerException()
}