package com.casontek.easyshop.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.casontek.easyshop.data.local.entity.Review

@Dao
interface ReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addReview(review: Review)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addReviews(reviews: List<Review>)

    @Query("SELECT * FROM Review")
    suspend fun reviews() : List<Review>

    @Query("SELECT * FROM Review WHERE product =:productId")
    suspend fun reviewsByProduct(productId: Int) : List<Review>

}