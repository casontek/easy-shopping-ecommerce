package com.casontek.easyshop.ui.pages.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.core.Amplify
import com.casontek.easyshop.data.local.dao.CartDao
import com.casontek.easyshop.data.local.dao.OrderDao
import com.casontek.easyshop.data.local.dao.ProductDao
import com.casontek.easyshop.data.local.entity.OrderHistory
import com.casontek.easyshop.data.local.model.ProductCart
import com.casontek.easyshop.ui.pages.main.MainState
import com.casontek.easyshop.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val productDao: ProductDao,
    private val cartDao: CartDao,
    private val orderDao: OrderDao
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
            orderDao.userOrders(userId).collect {
                updateState(_state.value.copy(orderHistory = it))
            }
        }
    }

    fun placeOrder() = viewModelScope.launch {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.US)
        val formatted = LocalDate.now().format(formatter)

        state.value.carts.forEach {
            val order = OrderHistory(
                productId = it.productId,
                userId = state.value.userId,
                title = it.title,
                date = formatted,
                quantity = it.quantity,
                price = it.price,
                image = it.images.first()
            )

            //create order
            orderDao.orderProduct(order)
            deleteCartItem(it.cartId)
        }
    }

    fun deleteCartItem(cartId: Int) = viewModelScope.launch {
        cartDao.removeCartItem(cartId)
    }

}