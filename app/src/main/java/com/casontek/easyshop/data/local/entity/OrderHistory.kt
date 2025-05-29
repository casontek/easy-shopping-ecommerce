package com.casontek.easyshop.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OrderHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productId: Int,
    val userId: String,
    val title: String,
    val date: String,
    val quantity: Int,
    val price: Double,
    val image: String
)
