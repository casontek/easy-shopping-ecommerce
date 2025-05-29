package com.casontek.easyshop.utils

sealed class ApiResult<out T : Any>
data class Success<out T : Any>(val data: T?) : ApiResult<T>()
data class Failure(
    val error: Exception,
    val message: String? = null
) : ApiResult<Nothing>()