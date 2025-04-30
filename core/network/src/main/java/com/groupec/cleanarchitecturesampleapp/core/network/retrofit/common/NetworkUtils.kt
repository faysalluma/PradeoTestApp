package com.groupec.cleanarchitecturesampleapp.core.network.retrofit.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

suspend fun <T, R> safeApiCall(apiCall: suspend () -> Response<T>, transform: (T) -> R): R {
    return withContext(Dispatchers.IO) {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                response.body()?.let {
                    return@withContext transform(it)
                } ?: throw Exception("Empty response body")
            } else {
                throw HttpException(response)
            }
        } catch (exception: IOException) { // Handle network errors
            throw exception
        } catch (exception: Exception) { // Handle other errors
            throw exception
        }
    }
}
