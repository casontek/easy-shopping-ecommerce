package com.casontek.easyshop.data.repository

import com.casontek.easyshop.data.local.entity.Product
import com.casontek.easyshop.utils.ApiResult


interface ProductRepository {
    suspend fun getAllProducts() : ApiResult<List<Product>>

    suspend fun productByCategory(category: String) : List<Product>

    suspend fun product(id: Int) : Product

    suspend fun deleteProduct(id: Int)
}