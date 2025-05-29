package com.casontek.easyshop.ui.pages.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.casontek.easyshop.data.local.entity.Product
import com.casontek.easyshop.data.repository.ProductRepository
import com.casontek.easyshop.ui.pages.entry.EntryState
import com.casontek.easyshop.utils.Failure
import com.casontek.easyshop.utils.Status
import com.casontek.easyshop.utils.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntryViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _state = MutableStateFlow<EntryState>(EntryState())
    val state: StateFlow<EntryState> = _state

    fun loadProducts() = viewModelScope.launch {
        updateState(_state.value.copy(status = Status.loading))
        productRepository.getAllProducts().let { result ->
            when(result) {
                is Success<List<Product>> -> {
                    Log.i("AGRO-SHOP", "@@@@@@@@@@@@@@ Network request successful. ${result.data}")
                    updateState(_state.value.copy(
                        status = Status.success,
                        products = result.data ?: emptyList()
                    ))
                }
                is Failure -> {
                    updateState(_state.value.copy(
                        status = Status.failed,
                        message = result.message
                            ?: "Internal server error"
                    ))
                }
            }
        }
    }

    private fun updateState(newState: EntryState) {
        _state.value = newState
    }
}