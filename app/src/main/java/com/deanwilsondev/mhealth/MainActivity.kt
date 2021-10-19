package com.deanwilsondev.mhealth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.messaging.FirebaseMessaging
import javax.net.ssl.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val data = intent.getStringExtra("heartRate")
        setHeartRate(data)

        loadSavedSettings()
        doFcmTokenRegistration()
    }

    fun loadSavedSettings() {
        val sharedPref = getSharedPreferences("apiSettings", Context.MODE_PRIVATE)
        ApiInteraction.Ip = sharedPref.getString("ipAddress", "127.0.0.1").toString()
        ApiInteraction.Port = sharedPref.getString("portNumber", "8000").toString()
    }

    fun setHeartRate(heartRate: String?) {
        if(heartRate == null){
            return
        }
        val heartRateTextView = findViewById<TextView>(R.id.tv_heartRateAmount)
        heartRateTextView.text = heartRate
    }

    fun doFcmTokenRegistration(){
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                val token = task.result as String

                val policy = ThreadPolicy.Builder()
                    .permitAll().build()
                StrictMode.setThreadPolicy(policy)
                try {
                    ApiInteraction.sendTokenToApi(token)
                } catch (e: Exception) {
                    // update server status here
                    ApiInteraction.Status = "Not Connected"
                }
            })
    }

    fun settings(view: android.view.View) {
        val intent = Intent(this, SettingsActivity::class.java)

        startActivity(intent)
    }
}