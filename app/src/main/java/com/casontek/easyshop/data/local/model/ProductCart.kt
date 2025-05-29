package com.casontek.easyshop.data.local.model

data class ProductCart(
    var cartId: Int,
    var productId: Int,
    var quantity: Int,
    var title: String,
    var price: Double,
    var images: List<String>
)
