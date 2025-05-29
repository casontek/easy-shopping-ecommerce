package com.casontek.easyshop.ui.pages.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.casontek.easyshop.data.local.dao.ProductDao
import com.casontek.easyshop.data.local.entity.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val productDao: ProductDao
) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    fun productsByCategory(category: String) = viewModelScope.launch {
        val productsList = productDao.productsByCategory(category)
        _products.value = productsList
    }
}