package com.project.firechatting

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val mfirebaseRemoteCOnfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val splash_background: String = mfirebaseRemoteCOnfig.getString("splash_background")

        window.statusBarColor = Color.parseColor(splash_background)

    }

}