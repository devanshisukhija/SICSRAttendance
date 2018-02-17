package com.devanshisukhija.sicsrattendance.Controller

import android.app.Application
import com.devanshisukhija.sicsrattendance.BuildConfig
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

/**
 * Created by devanshi on 14/02/18.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val options = FirebaseOptions.Builder()
                .setApplicationId(BuildConfig.APPLICATION_ID)
                .setApiKey("AIzaSyBUFy5QFOK1YDqJu4uatksBGX9nR2B4obM")
                .setDatabaseUrl("https://sicsr-d4771.firebaseio.com")
                .build()
            FirebaseApp.initializeApp(this, options)
    }
}