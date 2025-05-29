package com.casontek.easyshop.ui.pages.main.tabs

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.casontek.easyshop.data.local.model.ProductCart
import com.casontek.easyshop.navigation.Screen
import com.casontek.easyshop.ui.pages.viewmodel.MainViewModel
import com.casontek.easyshop.ui.theme.DarkYellow
import com.casontek.easyshop.ui.theme.White


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CartTab(
    viewModel: MainViewModel,
    controller: NavController
) {
    val state by viewModel.state.collectAsState()
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val source = remember { MutableInteractionSource() }
    val context = LocalContext.current

    // Decide number of columns based on screen width
    val columns = if (screenWidthDp < 600) 3 else 4
    val spacing = 8.dp
    val itemWidth = (screenWidthDp.dp - spacing * (columns + 1)) / columns

    if(state.carts.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 42.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Your bag is empty!\nAdd Product to your Cart",
                maxLines = 2,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = (1.5 * 16).sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )
        }
    }
    else {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize().padding(14.dp)
        ) {
            Column {
                if(state.carts.isNotEmpty()) {
                    Text(
                        "Available Carts",
                        maxLines = 1,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(Modifier.height(10.dp))
                }

                //display carts
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    repeat(state.carts.size) { index ->
                        CartItemComponent(
                            cart = state.carts[index],
                            itemWidth = (itemWidth - 8.dp),
                            onClicked = {
                                controller.navigate(Screen.Product.createRoute(
                                        state.carts[index].productId.toString()
                                    ))
                            },
                            onRemove = {
                                viewModel.deleteCartItem(it)
                            }
                        )
                    }
                }
            }

            //bottom order button
            Row(
                modifier = Modifier
                    .fillMaxWidth().height(56.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 14.dp)
                    .clickable(
                        interactionSource = source,
                        indication = null
                    ) {
                        Toast.makeText(
                            context,
                            "Orders currently not available!",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    imageVector = Icons.Outlined.ShoppingCart,
                    contentDescription = "add to cart",
                    colorFilter = ColorFilter.tint(Color.White)
                )

                Text(
                    "Place your Order",
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CartItemComponent(
    cart: ProductCart,
    itemWidth: Dp,
    onClicked: (cartId: Int) -> Unit,
    onRemove: (cartId: Int) -> Unit
) {
    val source = remember { MutableInteractionSource() }
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.width(itemWidth).wrapContentHeight().clickable(
                interactionSource = source,
                indication = null
            ) {
                onClicked(cart.productId)
            }
    ) {
        Column {
            val modifier = Modifier.fillMaxWidth().height(itemWidth - 10.dp)
            Box(
                modifier = modifier.background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                GlideImage(
                    model = cart.images.first(),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "product image",
                    modifier = modifier
                )

                Row(
                    modifier = Modifier.fillMaxSize().padding(8.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        "${cart.quantity}",
                        maxLines = 1,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    )
                }
            }

            Column(
                modifier = Modifier.padding(
                    horizontal = 4.dp,
                    vertical = 4.dp
                )
            ) {
                Text(
                    cart.title,
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

                Row {
                    Text(
                        "₦${cart.price}",
                        maxLines = 1,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    )

                    Spacer(Modifier.weight(1f))
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(4.dp)
                    .background(
                        color = DarkYellow,
                        shape = RoundedCornerShape(4.dp)
                    ).clickable(
                        interactionSource = source,
                        indication = null
                    ) {
                        onRemove(cart.cartId)
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Remove",
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = White
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}