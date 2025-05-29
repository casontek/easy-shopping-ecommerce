package com.casontek.easyshop.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Review(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val product: Int,
    var rating        : Int?    = null,
    var comment       : String? = null,
    var date          : String? = null,
    var reviewerName  : String? = null,
    var reviewerEmail : String? = null
)