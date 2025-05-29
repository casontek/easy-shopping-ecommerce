package com.casontek.easyshop.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cart(
    @PrimaryKey
    val cartId: Int,
    val userId: String,
    val quantity: Int
)