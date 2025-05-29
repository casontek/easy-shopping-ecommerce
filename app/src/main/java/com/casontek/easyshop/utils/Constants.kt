package com.casontek.easyshop.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

const val BASE_URL = "https://dummyjson.com"
const val BASE_PATH = "products/category"

fun Context.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}