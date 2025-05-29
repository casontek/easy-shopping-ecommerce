package com.casontek.easyshop.data.remote.model

import com.casontek.easyshop.data.local.entity.Review
import com.google.gson.annotations.SerializedName

data class ReviewDto (
    @SerializedName("rating"        ) var rating        : Int?    = null,
    @SerializedName("comment"       ) var comment       : String? = null,
    @SerializedName("date"          ) var date          : String? = null,
    @SerializedName("reviewerName"  ) var reviewerName  : String? = null,
    @SerializedName("reviewerEmail" ) var reviewerEmail : String? = null
)

fun ReviewDto.toReview(productId: Int): Review {
    return Review(
        product = productId,
        rating = this.rating ?: 0,
        comment = this.comment.orEmpty(),
        date = this.date.orEmpty(),
        reviewerName = this.reviewerName.orEmpty(),
        reviewerEmail = this.reviewerEmail.orEmpty()
    )
}

