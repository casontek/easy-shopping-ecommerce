package com.casontek.easyshop.ui.pages.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.core.Amplify
import com.casontek.easyshop.data.local.dao.CartDao
import com.casontek.easyshop.data.local.dao.ProductDao
import com.casontek.easyshop.ui.pages.main.MainState
import com.casontek.easyshop.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val productDao: ProductDao,
    private val cartDao: CartDao
): ViewModel() {
    private val _state = MutableStateFlow<MainState>(MainState())
    val state: StateFlow<MainState> = _state

    init {
        //load products
        fetchProducts()
        validateSignedUser()
    }

    private fun fetchProducts() = viewModelScope.launch {
        updateState(_state.value.copy(status = Status.loading))
        val smartphones = productDao.productsByCategory("smartphones")
        val laptops = productDao.productsByCategory("laptops")
        val motorcycle = productDao.productsByCategory("motorcycle")
        val vehicle = productDao.productsByCategory("vehicle")
        //update products state
        updateState(_state.value.copy(
            smartphones = smartphones,
            laptops = laptops,
            motorcycle = motorcycle,
            vehicle = vehicle,
            status = Status.success
        ))
    }

    fun onTabSelected(index: Int) {
        updateState(_state.value.copy(selectedTab = index))
        Log.i("MAIN", "@@@@@@@@@@@@@@@@@@@@@ selected tab: $index")
    }

    private fun updateState(newState: MainState) {
        _state.value = newState
    }

    fun validateSignedUser() {
        Amplify.Auth.fetchAuthSession(
            { session ->
                viewModelScope.launch {
                    if (session.isSignedIn) {
                        updateUserDetails()
                    }
                    else {
                        updateState(_state.value.copy(isSigned = false))
                    }
                }
            },
            { error ->
                viewModelScope.launch {
                    updateState(_state.value.copy(isSigned = false))
                }
            }
        )
    }

    private fun updateUserDetails() {
        Amplify.Auth.getCurrentUser({
            observeCartsWithProduct(it.userId)
            viewModelScope.launch {
                updateState(_state.value.copy(
                    isSigned = true,
                    username = it.username,
                    userId = it.userId
                ))
            }
        },{})
        Amplify.Auth.fetchUserAttributes(
            { attributes ->
                viewModelScope.launch {
                    val email = attributes.find { it.key.keyString == "email" }?.value
                    val name = attributes.find { it.key.keyString == "name" }?.value
                    updateState(_state.value.copy(
                        isSigned = true,
                        names = name ?: "",
                        email = email ?: ""
                    ))
                }
            },
            { error ->
                Log.e("AmplifyAuth", "Failed to fetch user attributes", error)
            }
        )
    }

    private fun observeCartsWithProduct(userId: String) {
        viewModelScope.launch {
            cartDao.getCartsWithProducts(userId).collect { carts ->
                updateState(_state.value.copy(carts = carts))
            }
        }
    }

    fun deleteCartItem(cartId: Int) = viewModelScope.launch {
        cartDao.removeCartItem(cartId)
    }

}