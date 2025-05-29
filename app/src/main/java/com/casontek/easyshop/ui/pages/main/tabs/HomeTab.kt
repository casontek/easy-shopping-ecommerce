package com.casontek.easyshop.ui.pages.main.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.casontek.easyshop.data.local.entity.Product
import com.casontek.easyshop.ui.pages.viewmodel.MainViewModel
import com.casontek.easyshop.utils.Status


@Composable
fun HomeTab(
    viewModel: MainViewModel,
    onProductSelected: (product: Product) -> Unit,
    onProductCategory: (category: String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    // Decide number of columns based on screen width
    val columns = if (screenWidthDp < 600) 2 else 3
    val spacing = 8.dp
    val itemWidth = (screenWidthDp.dp - spacing * (columns + 1)) / columns

    if(state.status == Status.loading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            CircularProgressIndicator(
                strokeCap = StrokeCap.Round,
                strokeWidth = 2.4.dp,
                color = Color.Gray
            )

            Spacer(Modifier.height(12.dp))

            Text(
                "Loading Products wait...",
                maxLines = 1,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
    else {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            if(state.smartphones.isNotEmpty()) {
                item {
                    ProductCategoryComponent(
                        products = state.smartphones,
                        itemWidth = itemWidth,
                        label = "Smartphones",
                        onSelected = {
                            onProductSelected(it)
                        },
                        onViewAll = {
                            onProductCategory(it)
                        }
                    )
                }
            }

            if(state.laptops.isNotEmpty()) {
                item {
                    ProductCategoryComponent(
                        products = state.laptops,
                        itemWidth = itemWidth,
                        label = "Laptops",
                        onSelected = {
                            onProductSelected(it)
                        },
                        onViewAll = {
                            onProductCategory(it)
                        }
                    )
                }
            }

            if(state.motorcycle.isNotEmpty()) {
                item {
                    ProductCategoryComponent(
                        products = state.motorcycle,
                        itemWidth = itemWidth,
                        label = "Motorcycle",
                        onSelected = {
                            onProductSelected(it)
                        },
                        onViewAll = {
                            onProductCategory(it)
                        }
                    )
                }
            }

            if(state.vehicle.isNotEmpty()) {
                item {
                    ProductCategoryComponent(
                        products = state.vehicle,
                        itemWidth = itemWidth,
                        label = "Motorcycle",
                        onSelected = {
                            onProductSelected(it)
                        },
                        onViewAll = {
                            onProductCategory(it)
                        }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductCategoryComponent(
    products: List<Product>,
    itemWidth: Dp,
    label: String,
    onSelected: (product: Product) -> Unit,
    onViewAll: (category: String) -> Unit
) {
    Column {

        Column(Modifier.padding(
            top = 14.dp,
            bottom = 6.dp
        )) {
            CategoryHeader(
                label = label,
                itemSize = products.size
            ) {
                //open category screen
                onViewAll(products.first().category)
            }
        }

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val count = if (products.size > 4) 4 else products.size
            repeat(count) { index ->
                ProductItemComponent(
                    product = products[index],
                    itemWidth = (itemWidth - 8.dp)
                ) {
                    onSelected(products[index])
                }
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductItemComponent(
    product: Product,
    itemWidth: Dp,
    onSelectedProduct: (id: Int) -> Unit
) {
    val source = remember { MutableInteractionSource() }
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.width(itemWidth).wrapContentHeight()
    ) {
        Column {
            val modifier = Modifier.fillMaxWidth().height((itemWidth.value + (itemWidth.value/5)).dp)
            Box(
                modifier = modifier
                    .background(Color.LightGray)
                    .clickable(
                        interactionSource = source,
                        indication = null
                    ) {
                        onSelectedProduct(product.productId)
                    },
                contentAlignment = Alignment.Center
            ) {
                GlideImage(
                    model = product.images.first(),
                    contentScale = ContentScale.Inside,
                    contentDescription = "product image",
                    modifier = modifier,
                    loading = placeholder {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                strokeCap = StrokeCap.Round,
                                color = MaterialTheme.colorScheme.secondary,
                                strokeWidth = 1.8.dp
                            )
                        }
                    }
                )
            }

            Column(
                modifier = Modifier.padding(
                    horizontal = 8.dp,
                    vertical = 8.dp
                )
            ) {
                Text(
                    product.title,
                    maxLines = 1,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    "₦${product.price}",
                    maxLines = 1,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                )
            }
        }
    }
}


@Composable
fun CategoryHeader(
    label: String,
    itemSize: Int,
    onSeeAll: () -> Unit
) {
    val source = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            maxLines = 1,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )

        if(itemSize > 6) {
            Text(
                "See All",
                maxLines = 1,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
                    .clickable(
                        interactionSource = source,
                        indication = null
                    ) {
                        onSeeAll()
                    }
            )
        }
    }
}

@Composable
@Preview
fun ProductItemComponentPrv() {
    ProductItemComponent(
        sampleProduct(),
        100.dp
    ){}
}

private fun sampleProduct() : Product {
    return Product(
        productId = 1,
        title = "Wireless Headphones",
        description = "High-quality wireless headphones with noise cancellation.",
        category = "Electronics",
        price = 199.99,
        discountPercentage = 10.0,
        rating = 4.5,
        stock = 50,
        tags = listOf("audio", "wireless", "bluetooth", "headphones"),
        brand = "AudioPro",
        sku = "AUDIOPRO-WH-001",
        weight = 250,
        warrantyInformation = "1 year manufacturer warranty",
        shippingInformation = "Ships in 2–3 business days",
        availabilityStatus = "In Stock",
        returnPolicy = "30-day return policy",
        minimumOrderQuantity = 1,
        images = listOf(
            "https://example.com/image1.jpg",
            "https://example.com/image2.jpg"
        ),
        thumbnail = "https://example.com/thumb.jpg"
    )

}