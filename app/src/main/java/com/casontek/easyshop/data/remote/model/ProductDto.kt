package com.casontek.easyshop.data.remote.model

import com.casontek.easyshop.data.local.entity.Product
import com.google.gson.annotations.SerializedName

data class ProductDto (
    @SerializedName("id"                   ) var id                   : Int?               = null,
    @SerializedName("title"                ) var title                : String?            = null,
    @SerializedName("description"          ) var description          : String?            = null,
    @SerializedName("category"             ) var category             : String?            = null,
    @SerializedName("price"                ) var price                : Double?            = null,
    @SerializedName("discountPercentage"   ) var discountPercentage   : Double?            = null,
    @SerializedName("rating"               ) var rating               : Double?            = null,
    @SerializedName("stock"                ) var stock                : Int?               = null,
    @SerializedName("tags"                 ) var tags                 : ArrayList<String>  = arrayListOf(),
    @SerializedName("brand"                ) var brand                : String?            = null,
    @SerializedName("sku"                  ) var sku                  : String?            = null,
    @SerializedName("weight"               ) var weight               : Int?               = null,
    @SerializedName("warrantyInformation"  ) var warrantyInformation  : String?            = null,
    @SerializedName("shippingInformation"  ) var shippingInformation  : String?            = null,
    @SerializedName("availabilityStatus"   ) var availabilityStatus   : String?            = null,
    @SerializedName("reviews"              ) var reviews              : ArrayList<ReviewDto> = arrayListOf(),
    @SerializedName("returnPolicy"         ) var returnPolicy         : String?            = null,
    @SerializedName("minimumOrderQuantity" ) var minimumOrderQuantity : Int?               = null,
    @SerializedName("images"               ) var images               : ArrayList<String>  = arrayListOf(),
    @SerializedName("thumbnail"            ) var thumbnail            : String?            = null
)

fun ProductDto.toProduct(): Product {
    return Product(
        productId = id ?: 0,
        title = title.orEmpty(),
        description = description.orEmpty(),
        category = category.orEmpty(),
        price = price ?: 0.0,
        discountPercentage = discountPercentage ?: 0.0,
        rating = rating ?: 0.0,
        stock = stock ?: 0,
        tags = tags,
        brand = brand.orEmpty(),
        sku = sku.orEmpty(),
        weight = weight ?: 0,
        warrantyInformation = warrantyInformation.orEmpty(),
        shippingInformation = shippingInformation.orEmpty(),
        availabilityStatus = availabilityStatus.orEmpty(),
        returnPolicy = returnPolicy.orEmpty(),
        minimumOrderQuantity = minimumOrderQuantity ?: 1,
        images = images,
        thumbnail = thumbnail.orEmpty()
    )
}
