package com.casontek.easyshop.ui.pages.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.core.Amplify
import com.casontek.easyshop.data.local.dao.CartDao
import com.casontek.easyshop.data.local.dao.ProductDao
import com.casontek.easyshop.data.local.dao.ReviewDao
import com.casontek.easyshop.data.local.entity.Cart
import com.casontek.easyshop.ui.pages.product.ProductState
import com.casontek.easyshop.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductViewModel @Inject constructor(
    private val dao: ProductDao,
    private val reviewDao: ReviewDao,
    private val cartDao: CartDao
) : ViewModel() {
    private val _state = MutableStateFlow<ProductState>(ProductState())
    val state: StateFlow<ProductState> = _state

    init {
        validateSignedUser()
    }

    fun loadProduct(productId: Int) = viewModelScope.launch {
        val product = dao.product(productId)
        val reviews = reviewDao.reviewsByProduct(productId)
        updateState(_state.value.copy(
            product = product,
            status = Status.success,
            reviews = reviews
        ))
    }

    private fun updateState(newState: ProductState) {
       _state.value = newState
    }

    fun addCart(productId: Int, quantity: Int) = viewModelScope.launch {
        updateState(_state.value.copy(cartStatus = Status.loading))
        try {
            val cart = Cart(
                cartId = productId,
                userId = state.value.userId,
                quantity = quantity
            )

            cartDao.insertCart(cart)

            updateState(_state.value.copy(
                cartStatus = Status.success,
                message = "Successfully added product to Cart."
            ))
        }
        catch (e: Exception) {
            updateState(_state.value.copy(
                cartStatus = Status.failed,
                message = "Failed to add product to Cart."
            ))
        }
    }

    fun validateSignedUser() {
        Amplify.Auth.fetchAuthSession(
            { session ->
                viewModelScope.launch {
                    if (session.isSignedIn) {
                        updateUserDetails()
                    }
                }
            },
            { error ->

            }
        )
    }

    private fun updateUserDetails() {
        Log.i("PRD","@@@@@@@@@@@@@@@@@ Updating signed user")
        Amplify.Auth.getCurrentUser({
            viewModelScope.launch {
                updateState(_state.value.copy(
                    signedIn = true,
                    userId = it.userId
                ))
            }
        },{})
    }

}