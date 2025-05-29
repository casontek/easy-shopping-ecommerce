package com.casontek.easyshop.data.remote.api

import com.casontek.easyshop.data.remote.model.ResponseBody
import com.casontek.easyshop.utils.BASE_PATH
import retrofit2.http.GET
import retrofit2.http.Path


interface RestApiClient {

    @GET("$BASE_PATH/{category}?limit=30")
    suspend fun productByCategory(@Path("category") category: String) : ResponseBody

}