package com.casontek.easyshop.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.casontek.easyshop.data.local.entity.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduct(product: Product)

    @Query("SELECT * FROM Product")
    suspend fun listProducts() : List<Product>

    @Query("SELECT * FROM Product WHERE productId =:id")
    suspend fun product(id: Int) : Product

    @Query("SELECT * FROM Product WHERE category =:category")
    suspend fun productsByCategory(category: String) : List<Product>

    @Query("SELECT * FROM Product WHERE title LIKE '%' || :keyword || '%'")
    fun searchProducts(keyword: String): Flow<List<Product>>


}