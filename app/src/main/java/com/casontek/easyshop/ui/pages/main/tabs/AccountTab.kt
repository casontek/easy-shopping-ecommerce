package com.casontek.easyshop.ui.pages.main.tabs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.amplifyframework.core.Amplify
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.casontek.easyshop.R
import com.casontek.easyshop.data.local.entity.OrderHistory
import com.casontek.easyshop.data.local.model.ProductCart
import com.casontek.easyshop.navigation.Screen
import com.casontek.easyshop.ui.pages.viewmodel.MainViewModel
import com.casontek.easyshop.ui.theme.DarkYellow
import com.casontek.easyshop.ui.theme.White


@Composable
fun AccountTab(
    viewModel: MainViewModel,
    controller: NavController
) {
    val state by viewModel.state.collectAsState()
    if(state.isSigned) {
        ProfileComponent(
            state.names,
            state.email,
            state.orderHistory
        ) {
            Amplify.Auth.signOut {
                viewModel.validateSignedUser()
            }
        }
    }
    else {
        AccountDefault(controller)
    }
}


@Composable
fun ProfileComponent(
    name: String,
    email: String,
    orderHistories: List<OrderHistory>,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(R.drawable.rabbit),
            contentDescription = "User Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Name
        Text(
            text = name,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Email
        Text(
            text = email,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(18.dp))

        if(orderHistories.isNotEmpty()) {
            Text(
                text = "My Order History",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                style = MaterialTheme.typography.titleLarge
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                items(orderHistories) {
                    OrderHistoryItemComponent(it)
                }
            }
        }

        // Logout Button
        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Logout",
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun AccountDefault(controller: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "You're not logged in",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    controller.navigate(Screen.Login.route)
                }
            ) {
                Text("Login / Register")
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun OrderHistoryItemComponent(orderHistory: OrderHistory) {
    val source = remember { MutableInteractionSource() }
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.fillMaxWidth().wrapContentHeight().clickable(
            interactionSource = source,
            indication = null
        ) {

        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GlideImage(
                model = orderHistory.image,
                contentScale = ContentScale.FillWidth,
                contentDescription = "product image",
                modifier = Modifier.width(100.dp)
            )

            Spacer(Modifier.width(10.dp))

            Column {
                Text(
                    orderHistory.title,
                    maxLines = 2,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "₦${orderHistory.price}",
                        maxLines = 1,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    )

                    Text(
                        "Quantity: ${orderHistory.quantity}",
                        maxLines = 1,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        orderHistory.date,
                        maxLines = 1,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ProfileComponentPrv() {
    ProfileComponent(
        "Agbo Chika Kingsley",
        "chika.agbokings@gmail.com",
        onLogout = {},
        orderHistories = emptyList()
    )
}

@Composable
@Preview(showBackground = true)
fun AccountDefaultPrv() {
    AccountDefault(rememberNavController())
}