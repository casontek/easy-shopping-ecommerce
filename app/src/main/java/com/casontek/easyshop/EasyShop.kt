package com.casontek.easyshop

import android.app.Application
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EasyShop: Application() {
    override fun onCreate() {
        super.onCreate()

        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(applicationContext)

            Log.i("AuthQuickStart", "Initialized Amplify")
        }
        catch (error: AmplifyException) {
            Log.e("AuthQuickStart", "Could not initialize Amplify", error)
        }
    }
}