package com.casontek.easyshop.ui.pages.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.amplifyframework.core.Amplify
import com.casontek.easyshop.navigation.Screen
import com.casontek.easyshop.ui.pages.product.showSnackBar
import com.casontek.easyshop.ui.theme.Grey01
import com.casontek.easyshop.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun EmailVerificationScreen(
    controller: NavController,
    email: String
) {
    var email by remember { mutableStateOf(email) }
    val scope = rememberCoroutineScope()
    var code by remember { mutableStateOf("") }
    var snackBarColor by remember { mutableStateOf(Red) }
    var isLoading by remember { mutableStateOf(false) }
    var disableResend by remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState
            ) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor =  snackBarColor,
                    contentColor = White
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        LazyColumn {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .padding(horizontal = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(96.dp))

                    Column {
                        Text (
                            text = "Verify your email",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "We’ve sent a 6-digit code to $email",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            ),
                            modifier = Modifier.padding(bottom = 24.dp)
                        )

                        OutlinedTextField(
                            value = code,
                            onValueChange = {
                                if (it.length <= 6) code = it
                            },
                            label = { Text("Enter verification code") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                isLoading = true
                                Amplify.Auth.confirmSignUp(email, code,
                                    {
                                        CoroutineScope(Dispatchers.Main).launch{
                                            snackBarColor = Green
                                            showSnackBar("Account verified successfully.", scope, snackBarHostState)
                                            controller.navigate(Screen.Login.route)
                                        }
                                    },
                                    {
                                        CoroutineScope(Dispatchers.Main).launch{
                                            isLoading = false
                                            snackBarColor = Red
                                            showSnackBar(it.message ?: "Account verification failed.", scope, snackBarHostState)
                                        }
                                    }
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = code.length == 6 && !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            else {
                                Text("Verify")
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                "Did not receive code?",
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            TextButton(
                                onClick = {
                                    if(!disableResend) {
                                        Amplify.Auth.resendSignUpCode(email, {
                                            CoroutineScope(Dispatchers.Main).launch{
                                                snackBarColor = Green
                                                showSnackBar("Verification code successfully sent.", scope, snackBarHostState)
                                                disableResend = true
                                            }
                                        }, {
                                            CoroutineScope(Dispatchers.Main).launch{
                                                snackBarColor = Red
                                                showSnackBar("Verification not sent.", scope, snackBarHostState)
                                            }
                                        })
                                    }
                                }
                            ) {
                                Text(
                                    "Resend code",
                                    color = if(!disableResend) Blue else Grey01
                                )
                            }
                        }

                    }

                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun EmailVerificationScreenPrv() {
    EmailVerificationScreen(
        rememberNavController(),
        "chika.agbokings@gmail.com"
    )
}