package com.casontek.easyshop.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.casontek.easyshop.data.local.entity.Cart
import com.casontek.easyshop.data.local.model.ProductCart
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cart: Cart)

    @Query("SELECT cart.cartId as cartId, product.productId as productId," +
            "cart.quantity as quantity, product.title as title, product.images as images," +
            "product.price as price FROM cart, product WHERE cart.userId =:userId AND cart.cartId == product.productId")
    fun getCartsWithProducts(userId: String): Flow<List<ProductCart>>

    @Query("SELECT * FROM cart WHERE cart.cartId = :id AND cart.userId = :userId")
    suspend fun getCartItem(id: Int, userId: String): Cart?

    @Query("DELETE FROM cart WHERE cart.cartId =:id")
    fun removeCartItem(id: Int): Int

}