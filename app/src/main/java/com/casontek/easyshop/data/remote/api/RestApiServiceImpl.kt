package com.casontek.easyshop.data.remote.api

import javax.inject.Inject


class RestApiServiceImpl @Inject constructor(
    private val restApiClient: RestApiClient
) : RestApiService {

    override suspend fun productByCategory(category: String) = restApiClient
        .productByCategory(category)
}