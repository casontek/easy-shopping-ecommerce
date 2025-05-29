package com.casontek.easyshop.ui.pages.main

import com.casontek.easyshop.data.local.entity.OrderHistory
import com.casontek.easyshop.data.local.entity.Product
import com.casontek.easyshop.data.local.model.ProductCart
import com.casontek.easyshop.utils.Status

data class MainState(
    var selectedTab: Int = 1,
    var userId: String = "",
    var username: String = "",
    var email: String = "",
    var names: String = "",
    var isSigned: Boolean = false,
    var status: Status = Status.initial,
    var smartphones: List<Product> = emptyList(),
    var laptops: List<Product> = emptyList(),
    var motorcycle: List<Product> = emptyList(),
    var vehicle: List<Product> = emptyList(),
    var carts: List<ProductCart> = emptyList(),
    var orderHistory: List<OrderHistory> = emptyList()
)