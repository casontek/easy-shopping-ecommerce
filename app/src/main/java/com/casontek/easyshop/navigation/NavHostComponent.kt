package com.casontek.easyshop.navigation


import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.casontek.easyshop.ui.pages.addItem.NewProductScreen
import com.casontek.easyshop.ui.pages.auth.EmailVerificationScreen
import com.casontek.easyshop.ui.pages.auth.LoginScreen
import com.casontek.easyshop.ui.pages.auth.RegisterScreen
import com.casontek.easyshop.ui.pages.category.ProductCategoryScreen
import com.casontek.easyshop.ui.pages.entry.EntryScreen
import com.casontek.easyshop.ui.pages.main.HomeScreen
import com.casontek.easyshop.ui.pages.product.ProductScreen
import com.casontek.easyshop.ui.pages.viewmodel.CategoryViewModel
import com.casontek.easyshop.ui.pages.viewmodel.EntryViewModel
import com.casontek.easyshop.ui.pages.viewmodel.MainViewModel
import com.casontek.easyshop.ui.pages.viewmodel.ProductViewModel
import java.util.Locale

@Composable
fun NavHostComponent(
    controller: NavHostController
) {
    NavHost(
        navController = controller,
        startDestination = Screen.Entry.route
    ) {
        composable(route = Screen.Entry.route) {
            val model = hiltViewModel<EntryViewModel>()
            model.loadProducts()
            EntryScreen(
                controller,
                model
            )
        }

        composable(route = Screen.Main.route) {
            val model = hiltViewModel<MainViewModel>()
            HomeScreen(
                viewModel = model,
                controller = controller
            )
        }

        composable(
            route = Screen.Product.route,
            arguments = listOf(navArgument("productId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId")
            val model = hiltViewModel<ProductViewModel>()
            model.loadProduct(productId ?: 0)

            ProductScreen(model, controller)
        }

        composable(route = Screen.NewItem.route) {
            NewProductScreen()
        }

        composable(route = Screen.Login.route) {
            LoginScreen(controller)
        }

        composable(route = Screen.Register.route) {
            RegisterScreen(controller)
        }

        composable(
            route = Screen.EmailVerification.route,
            arguments = listOf(navArgument("email") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            EmailVerificationScreen(controller, email ?: "")
        }

        composable(
            route = Screen.ProductCategory.route,
            arguments = listOf(navArgument("category") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")
            val model = hiltViewModel<CategoryViewModel>()
            model.productsByCategory(category?.toLowerCase(Locale.ROOT) ?: "smartphones")

            ProductCategoryScreen(
                controller,
                category ?: "",
                model
            )
        }

    }
}