package com.casontek.easyshop.ui.pages.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.casontek.easyshop.navigation.Screen
import com.casontek.easyshop.ui.pages.entry.SystemBarComponent
import com.casontek.easyshop.ui.pages.main.tabs.AccountTab
import com.casontek.easyshop.ui.pages.main.tabs.CartTab
import com.casontek.easyshop.ui.pages.main.tabs.HomeTab
import com.casontek.easyshop.ui.pages.viewmodel.MainViewModel
import com.casontek.easyshop.ui.theme.DarkYellow
import com.casontek.easyshop.ui.theme.White

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    controller: NavController
){
    val state by viewModel.state.collectAsState()
    val isDarkMode = isSystemInDarkTheme()

    SystemBarComponent(
        statusColor = MaterialTheme.colorScheme.primary.toArgb(),
        navColor = MaterialTheme.colorScheme.background.toArgb(),
        isLightMode = !isDarkMode
    )

    Scaffold(
        modifier = Modifier.fillMaxSize().systemBarsPadding(),
        topBar = {
            Column(
                Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 10.dp)
                ) {
                    Text(
                        "EasyShopping",
                        maxLines = 1,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )
                    )

                    CartBadgeComponent(state.carts.size)
                }
            }
        },
        bottomBar = {
            BottomNavComponent(
                selectedIndex = state.selectedTab,
            ) {
                viewModel.onTabSelected(it)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            when(state.selectedTab) {
                1 -> {
                    HomeTab(
                        viewModel = viewModel,
                        onProductSelected = {
                            //open product detail
                            controller.navigate(Screen.Product.createRoute(it.productId.toString()))
                        },
                        onProductCategory = {
                            //open category detail page
                            controller.navigate(Screen.ProductCategory.createRoute(it))
                        }
                    )
                }
                2 -> {
                    CartTab(viewModel, controller)
                }
                else -> {
                    AccountTab(viewModel, controller)
                }
            }
        }
    }
}

@Composable
fun BottomNavComponent(
    selectedIndex: Int,
    tabSelectedCallback: (index: Int) -> Unit
) {
    val mutableInteractionSource = remember { MutableInteractionSource() }
    Row(
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //home bottom navItem
        BottomNavItemComponent(
            label = "Home",
            icon = Icons.Outlined.Home,
            isSelected = selectedIndex == 1,
            modifier = Modifier.padding(all = 8.dp)
                .clickable(
                    interactionSource = mutableInteractionSource,
                    indication = null
                ) {
                    tabSelectedCallback(1)
                }
        )

        Spacer(Modifier.weight(1f))

        //cart bottom navItem
        BottomNavItemComponent(
            label = "Cart",
            icon = Icons.Outlined.ShoppingCart,
            isSelected = selectedIndex == 2,
            modifier = Modifier.padding(all = 8.dp)
                .clickable(
                    interactionSource = mutableInteractionSource,
                    indication = null
                ){
                    tabSelectedCallback(2)
                }
        )

        Spacer(Modifier.weight(1f))

        //account bottom navItem
        BottomNavItemComponent(
            label = "Account",
            icon = Icons.Outlined.AccountCircle,
            isSelected = selectedIndex == 3,
            modifier = Modifier.padding(all = 8.dp)
                .clickable(
                    interactionSource = mutableInteractionSource,
                    indication = null
                ){
                    tabSelectedCallback(3)
                }
        )
    }
}

@Composable
fun BottomNavItemComponent(
    modifier: Modifier,
    icon: ImageVector,
    label: String,
    isSelected: Boolean = false
) {
    val viewColor = if(isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inversePrimary

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "navItem icon",
            tint = viewColor
        )

        Text(
            label,
            textAlign = TextAlign.Center,
            color = viewColor
        )
    }
}

@Composable
fun CartBadgeComponent(quantity: Int) {
    Box(
        modifier = Modifier.width(28.dp).height(28.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Image(
            imageVector = Icons.Filled.ShoppingCart,
            contentDescription = "add to cart",
            colorFilter = ColorFilter.tint(White)
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(2.dp),
            horizontalAlignment = Alignment.End
        ) {
            Column(
                Modifier.background(
                    color = DarkYellow,
                    shape = RoundedCornerShape(35.dp)
                )
            ) {
                Text(
                    "$quantity",
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    ),
                    modifier = Modifier.wrapContentSize()
                        .padding(horizontal = 3.4.dp)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPrv() {
    HomeScreen(
        hiltViewModel<MainViewModel>(),
        rememberNavController()
    )
}

@Composable
@Preview(showBackground = true)
fun CartBadgeComponentPrv() {
    CartBadgeComponent(1)
}