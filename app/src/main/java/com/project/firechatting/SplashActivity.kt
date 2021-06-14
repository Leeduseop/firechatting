package com.project.firechatting

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.project.firechatting.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private var remoteConfig = Firebase.remoteConfig

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.default_config)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result


                    Toast.makeText(this, "Fetch and activate succeeded", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, "Fetch failed", Toast.LENGTH_SHORT).show()
                }
                displayMessage()
            }
    }

    fun displayMessage() {
        val splash_background: String = remoteConfig.getString("splash_background")
        val caps: Boolean = remoteConfig.getBoolean("splash_message_caps")
        val splash_message: String = remoteConfig.getString("splash_message")
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        binding.constLayout.setBackgroundColor(Color.parseColor(splash_background))

        if (caps) {
            builder.setMessage(splash_message).setPositiveButton(
                "확인",
                DialogInterface.OnClickListener { _, i ->
                    finish()
                })
        }else {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        builder.show()
    }

}