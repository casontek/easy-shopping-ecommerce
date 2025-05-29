package com.casontek.easyshop.ui.pages.entry

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.casontek.easyshop.R
import com.casontek.easyshop.navigation.Screen
import com.casontek.easyshop.ui.pages.viewmodel.EntryViewModel
import com.casontek.easyshop.ui.theme.White
import com.casontek.easyshop.utils.Status
import kotlinx.coroutines.launch

@Composable
fun EntryScreen(
    controller: NavController,
    viewModel: EntryViewModel
) {
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    SystemBarComponent()

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState
            ) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = Color.Red,
                    contentColor = White
                )
            }
        }
    ) { padding ->
        Box {
            Column(
                modifier = Modifier.fillMaxSize().background(
                    color = Color.White
                ).padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "EasyShop logo",
                    modifier = Modifier.width(96.dp).height(96.dp)
                )
            }

            //shows loading animation at the bottom
            if(state.status == Status.loading) {
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        strokeCap = StrokeCap.Round,
                        strokeWidth = 2.dp
                    )
                }
            }
        }
    }

    //navigates to the home screen after 10seconds
    LaunchedEffect(state.status) {
        if (state.status == Status.success) {
            controller.navigate(Screen.Main.route) {
                popUpTo(Screen.Entry.route) {
                    inclusive = true
                }
            }
        }
        else if (state.status == Status.failed) {
            scope.launch {
                val result = snackBarHostState
                    .showSnackbar(
                        message = state.message,
                        actionLabel = "Retry",
                        duration = SnackbarDuration.Long
                    )
                when (result) {
                    SnackbarResult.ActionPerformed -> {
                        viewModel.loadProducts()
                    }

                    SnackbarResult.Dismissed -> TODO()
                }
            }
        }
    }

}


@Composable
fun SystemBarComponent(
    statusColor: Int = Color.White.toArgb(),
    navColor: Int = Color.White.toArgb(),
    isLightMode: Boolean = false
) {
    val context = LocalContext.current
    val window = (context as Activity).window

    SideEffect {
        window.navigationBarColor = navColor
        window.statusBarColor = statusColor

        // Set icon color (dark or light)
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = isLightMode
        insetsController.isAppearanceLightNavigationBars = isLightMode
    }
}

@Composable
@Preview(showSystemUi = true)
fun EntryScreenPrv() {
    val model = hiltViewModel<EntryViewModel>()
    EntryScreen(
        rememberNavController(),
        model
    )
}