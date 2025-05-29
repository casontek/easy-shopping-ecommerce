package com.casontek.easyshop.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.casontek.easyshop.data.local.entity.OrderHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun orderProduct(order: OrderHistory)

    @Query("SELECT * FROM OrderHistory WHERE userId =:userId")
    fun userOrders(userId: String) : Flow<List<OrderHistory>>

}