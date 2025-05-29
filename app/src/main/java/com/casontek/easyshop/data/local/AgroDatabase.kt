package com.casontek.easyshop.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.casontek.easyshop.data.local.dao.CartDao
import com.casontek.easyshop.data.local.dao.ProductDao
import com.casontek.easyshop.data.local.dao.ReviewDao
import com.casontek.easyshop.data.local.entity.Cart
import com.casontek.easyshop.data.local.entity.Product
import com.casontek.easyshop.data.local.entity.Review
import com.casontek.easyshop.utils.Converters

@Database(version = 1, entities = [
    Product::class,
    Review::class,
    Cart::class
])
@TypeConverters(Converters::class)
abstract class AgroDatabase : RoomDatabase() {
    abstract fun productDao() : ProductDao
    abstract fun reviewDao() : ReviewDao
    abstract fun cartDao() : CartDao
}