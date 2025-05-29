package com.casontek.easyshop.ui.pages.entry

import com.casontek.easyshop.data.local.entity.Product
import com.casontek.easyshop.utils.Status

data class EntryState(
    var status: Status = Status.initial,
    var message: String = "",
    var products: List<Product> = emptyList<Product>()
)