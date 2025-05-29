package com.casontek.easyshop.data.remote.model

import com.google.gson.annotations.SerializedName

data class ResponseBody (
    @SerializedName("products" ) var products : ArrayList<ProductDto> = arrayListOf(),
    @SerializedName("total"    ) var total    : Int?                = null,
    @SerializedName("skip"     ) var skip     : Int?                = null,
    @SerializedName("limit"    ) var limit    : Int?                = null,
    @SerializedName("message"    ) var message: String?         = null
)