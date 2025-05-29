package com.casontek.easyshop.ui.pages.product

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.casontek.easyshop.R
import com.casontek.easyshop.navigation.Screen
import com.casontek.easyshop.ui.pages.entry.SystemBarComponent
import com.casontek.easyshop.ui.pages.viewmodel.ProductViewModel
import com.casontek.easyshop.ui.theme.Black
import com.casontek.easyshop.ui.theme.DarkYellow
import com.casontek.easyshop.ui.theme.Greenish
import com.casontek.easyshop.ui.theme.Grey01
import com.casontek.easyshop.ui.theme.White
import com.casontek.easyshop.utils.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductScreen(
    model: ProductViewModel,
    controller: NavController
) {
    val state by model.state.collectAsState()
    val isDarkMode = isSystemInDarkTheme()
    val source = remember { MutableInteractionSource() }
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    var cartQuantity = remember { mutableIntStateOf(1) }
    var snackBarColor by remember { mutableStateOf(Red) }
    val context = LocalContext.current
    val activity = context as Activity

    SystemBarComponent(
        statusColor = MaterialTheme.colorScheme.primary.toArgb(),
        navColor = MaterialTheme.colorScheme.background.toArgb(),
        isLightMode = !isDarkMode
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        bottomBar = {
            if(state.product != null) {
                Column(
                    Modifier.padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 14.dp)
                            .clickable(
                                interactionSource = source,
                                indication = null
                            ) {
                                if (state.signedIn) {
                                    model.addCart(
                                        state.product!!.productId,
                                        cartQuantity.intValue
                                    )
                                }
                                else {
                                    scope.launch {
                                        snackBarColor = Red
                                        var result = snackBarHostState.showSnackbar(
                                            message = "Please you need to SignIn first.",
                                            duration = SnackbarDuration.Long,
                                            actionLabel = "SIGN-IN"
                                        )

                                        when (result) {
                                            SnackbarResult.ActionPerformed -> {
                                                controller.navigate(Screen.Login.route)
                                            }

                                            SnackbarResult.Dismissed -> {

                                            }
                                        }
                                    }
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            imageVector = Icons.Outlined.ShoppingCart,
                            contentDescription = "add to cart",
                            colorFilter = ColorFilter.tint(Color.White)
                        )

                        Text(
                            "Add To Cart",
                            maxLines = 1,
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = White
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        Image(
                            painter = painterResource(R.drawable.minus),
                            contentDescription = "cart quantity",
                            colorFilter = ColorFilter.tint(if(cartQuantity.intValue > 1) Black else Grey01),
                            modifier = Modifier
                                .width(32.dp)
                                .height(32.dp)
                                .clickable(
                                    interactionSource = source,
                                    indication = null
                                ) {
                                    if (cartQuantity.intValue > 1) cartQuantity.intValue -= 1
                                }
                        )

                        Column(
                            Modifier.padding(horizontal = 10.dp)
                        ) {
                            Text(
                                "${cartQuantity.intValue}",
                                maxLines = 1,
                                textAlign = TextAlign.Center,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Black
                                )
                            )
                        }

                        Image(
                            painter = painterResource(R.drawable.addition),
                            contentDescription = "cart quantity",
                            modifier = Modifier
                                .width(32.dp)
                                .height(32.dp)
                                .clickable(
                                    interactionSource = source,
                                    indication = null
                                ) {
                                    cartQuantity.intValue += 1
                                }
                        )

                    }
                }
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState
            ) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = snackBarColor,
                    contentColor = White,
                    actionColor = Black
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Top
        ){
            if(state.status == Status.success) {
                val product = state.product!!
                LazyColumn {
                    item {
                        Column {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(320.dp)
                                    .background(Color.White),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                GlideImage(
                                    model = product.images.first(),
                                    contentScale = ContentScale.Inside,
                                    contentDescription = "product image",
                                    requestBuilderTransform = { request ->
                                        request.thumbnail(0.25f)
                                    }
                                )
                            }
                        }
                    }

                    item {
                        Column(
                            Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Text(
                                product.title,
                                textAlign = TextAlign.Start,
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                "Cost Price: ₦${product.price}",
                                maxLines = 1,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            )

                            Spacer(Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RatingBar(
                                    rating = product.rating,
                                    modifier = Modifier.wrapContentWidth()
                                )

                                Spacer(Modifier.width(6.dp))

                                if(state.reviews.isNotEmpty()){
                                    Text(
                                        "${state.reviews.size} ratings",
                                        maxLines = 1,
                                        style = TextStyle(
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.inversePrimary
                                        )
                                    )
                                }
                            }

                            Spacer(Modifier.height(12.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .background(
                                        color = MaterialTheme.colorScheme.surface,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                            ) {
                                Text(
                                    product.description,
                                    textAlign = TextAlign.Start,
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = MaterialTheme.colorScheme.onSurface
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.0.dp)
                                )
                            }
                        }
                    }
                }
            }
            else if(state.status == Status.failed) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Products Not found.",
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
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        strokeCap = StrokeCap.Round,
                        strokeWidth = 2.4.dp,
                        color = Color.Gray
                    )
                }
            }
        }
    }

    LaunchedEffect(state.cartStatus) {
        if(state.cartStatus == Status.success) {
            scope.launch {
                snackBarColor = Greenish
                snackBarHostState.showSnackbar(
                        message = state.message,
                        duration = SnackbarDuration.Short
                )
            }
        }
    }
}


fun showSnackBar(
    message: String,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState
) {
    scope.launch {
        snackBarHostState.showSnackbar(
            message = message,
            duration = SnackbarDuration.Short
        )
    }
}


@Composable
fun RatingBar(
    rating: Double,
    modifier: Modifier = Modifier,
    starCount: Int = 5,
    starSize: Dp = 24.dp,
    starColor: Color = DarkYellow
) {
    val roundedRating = rating.roundToInt()
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        for (i in 1..starCount) {
            val icon = if (i <= roundedRating) Icons.Filled.Star else Icons.Outlined.Star
            Icon(
                imageVector = icon,
                contentDescription = if (i <= roundedRating) "Full Star" else "Empty Star",
                tint = starColor,
                modifier = Modifier.size(starSize)
            )
        }
    }
}


