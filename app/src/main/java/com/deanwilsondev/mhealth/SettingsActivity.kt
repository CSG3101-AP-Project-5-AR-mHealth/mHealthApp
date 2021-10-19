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

        val statusEditText = findViewById<TextInputEditText>(R.id.status)
        statusEditText.setText(ApiInteraction.Status)
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
        if(validate()) {
            return
        }
        saveSettings()
        switchBackToMain()
    }

    // validates ip address input and port input
    fun validate(): Boolean {
        var hasError = false
        val ipAddressEditText = findViewById<TextInputEditText>(R.id.ipAddress)
        val ip = ipAddressEditText.text?.toString() ?: ""

        val ipPattern = "^(?:\\d{1,3}\\.){3}\\d{1,3}$"
        val regex = Regex(ipPattern)
        if(regex.matchEntire(ip) == null)         {
            ipAddressEditText.error = "Not a valid ip address."
            hasError = true
        }
        else  {
            ipAddressEditText.error = null
        }

        val portAddressEditText = findViewById<TextInputEditText>(R.id.portNumber)
        val port = portAddressEditText.text?.toString() ?: ""

        val portPattern = "\\d+"
        val portRegex = Regex(portPattern)
        if(portRegex.matchEntire(port) == null) {
            portAddressEditText.error = "Not a valid number."
            hasError = true
        }
        else {
            portAddressEditText.error = null
        }

        return hasError
    }

    fun cancel(view: android.view.View) {
        switchBackToMain()
    }
}