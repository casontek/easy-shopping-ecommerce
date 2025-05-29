package com.casontek.easyshop.data.remote.api

import com.casontek.easyshop.data.remote.model.ResponseBody

interface RestApiService {
    suspend fun productByCategory(category: String) : ResponseBody
}