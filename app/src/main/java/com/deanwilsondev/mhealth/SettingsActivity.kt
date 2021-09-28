package com.deanwilsondev.mhealth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        val ipAddressEditText = findViewById<TextInputEditText>(R.id.ipAddress)
        ipAddressEditText.setText(ApiInteraction.Ip)

        val portAddressEditText = findViewById<TextInputEditText>(R.id.portNumber)
        portAddressEditText.setText(ApiInteraction.Port)
    }

    fun saveSettings() {
        val ipAddressEditText = findViewById<TextInputEditText>(R.id.ipAddress)
        ApiInteraction.Ip = ipAddressEditText.editableText.toString()

        val portAddressEditText = findViewById<TextInputEditText>(R.id.portNumber)
        ApiInteraction.Port = portAddressEditText.editableText.toString()

        val sharedPref = getSharedPreferences("apiSettings", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("ipAddress", ApiInteraction.Ip)
            putString("portNumber", ApiInteraction.Port)
            commit()
        }
    }

    private fun switchBackToMain() {
        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent)
    }

    fun submit(view: android.view.View) {
        saveSettings()
        switchBackToMain()
    }
    fun cancel(view: android.view.View) {
        switchBackToMain()
    }
}