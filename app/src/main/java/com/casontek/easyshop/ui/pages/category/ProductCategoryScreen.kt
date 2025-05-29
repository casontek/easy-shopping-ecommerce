package com.casontek.easyshop.ui.pages.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.casontek.easyshop.navigation.Screen
import com.casontek.easyshop.ui.pages.main.tabs.ProductItemComponent
import com.casontek.easyshop.ui.pages.viewmodel.CategoryViewModel
import com.casontek.easyshop.ui.theme.White


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProductCategoryScreen(
    controller: NavController,
    category: String,
    viewModel: CategoryViewModel
) {
    val products by viewModel.products.collectAsState()
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val columns = if (screenWidthDp < 600) 2 else 3
    val spacing = 8.dp
    val itemWidth = (screenWidthDp.dp - spacing * (columns + 1)) / columns

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    category,
                    color = White
                ) },
                navigationIcon = {
                    IconButton(onClick = {
                        controller.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        // Main content
        LazyColumn (
            modifier = Modifier.padding(innerPadding).fillMaxSize()
                .padding(all = 12.dp)
        ) {
            item {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    repeat(products.size) { index ->
                        ProductItemComponent(
                            product = products[index],
                            itemWidth = (itemWidth - 8.dp)
                        ) {
                            //open product detail
                            controller.navigate(
                                Screen.Product.createRoute(products[index].productId.toString())
                            )
                        }
                    }
                }
            }
        }
    }
}