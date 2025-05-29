package com.casontek.easyshop.ui.pages.product

import com.casontek.easyshop.data.local.entity.Product
import com.casontek.easyshop.data.local.entity.Review
import com.casontek.easyshop.utils.Status

data class ProductState(
    var product: Product? = null,
    var status: Status = Status.initial,
    var cartStatus: Status = Status.initial,
    var message: String = "",
    var signedIn: Boolean = false,
    var userId: String = "",
    var reviews: List<Review> = emptyList()
)
