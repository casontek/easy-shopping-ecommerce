package com.casontek.easyshop.navigation

sealed class Screen(val route: String) {
    object Main: Screen(route = "main")
    object Product: Screen(route = "product/{productId}") {
        fun createRoute(productId: String) = "product/$productId"
    }
    object Entry: Screen(route = "launcher")
    object NewItem: Screen(route = "add-item")
    object Login: Screen(route = "login")
    object Register: Screen(route = "register")
    object EmailVerification: Screen(route = "verification/{email}") {
        fun createRoute(email: String) = "verification/$email"
    }
    object ProductCategory: Screen(route = "product-category/{category}") {
        fun createRoute(category: String) = "product-category/$category"
    }
}