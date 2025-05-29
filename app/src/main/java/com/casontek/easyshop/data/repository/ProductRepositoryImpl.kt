package com.casontek.easyshop.data.repository

import android.content.Context
import com.casontek.easyshop.data.local.dao.ProductDao
import com.casontek.easyshop.data.local.dao.ReviewDao
import com.casontek.easyshop.data.local.entity.Product
import com.casontek.easyshop.data.remote.api.RestApiService
import com.casontek.easyshop.data.remote.model.ProductDto
import com.casontek.easyshop.data.remote.model.toProduct
import com.casontek.easyshop.data.remote.model.toReview
import com.casontek.easyshop.utils.ApiResult
import com.casontek.easyshop.utils.Failure
import com.casontek.easyshop.utils.Success
import com.casontek.easyshop.utils.isInternetAvailable
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val restApiService: RestApiService,
    private val productDao: ProductDao,
    private val reviewDao: ReviewDao,
    @ApplicationContext private val context: Context
) : ProductRepository{

    override suspend fun getAllProducts(): ApiResult<List<Product>> {
        try {
            val localProducts = productDao.listProducts()
            if(localProducts.isEmpty()) {
                if(context.isInternetAvailable()) {
                    //fetch smartphones, laptops, motorcycle, and
                    val smartphones = restApiService.productByCategory("smartphones")
                    val laptops = restApiService.productByCategory("laptops")
                    val motorcycle = restApiService.productByCategory("motorcycle")
                    val vehicle = restApiService.productByCategory("vehicle")

                    //save products
                    saveProducts(smartphones.products)
                    saveProducts(laptops.products)
                    saveProducts(motorcycle.products)
                    saveProducts(vehicle.products)

                    return Success(productDao.listProducts())
                }
                else {
                    return Failure(
                        Exception("No internet connection."),
                        "No internet connection."
                    )
                }
            }
            else {
                return Success<List<Product>>(localProducts)
            }
        }
        catch (e: Exception) {
            return Failure(e, e.message)
        }
    }

    override suspend fun productByCategory(category: String): List<Product> {
        try {
            val products = productDao.productsByCategory(category)
            if(products.isEmpty()) {
                val result = restApiService.productByCategory(category)
                saveProducts(result.products)

                return productDao.productsByCategory(category)
            }
            else return products
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    override suspend fun product(id: Int): Product {
        return  productDao.product(id)
    }

    override suspend fun deleteProduct(id: Int) {
        productDao.product(id)
    }

    private suspend fun saveProducts(products: List<ProductDto>) {
        products.forEach { product ->
            val productReviews = product.reviews
            //save product
            productDao.addProduct(product.toProduct())
            reviewDao.addReviews(productReviews.map { it.toReview(product.id ?: 0) })
        }
    }

}