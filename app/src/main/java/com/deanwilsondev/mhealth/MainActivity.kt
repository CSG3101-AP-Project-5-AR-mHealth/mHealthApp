package com.deanwilsondev.mhealth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.deanwilsondev.mhealth.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var token = ""
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                token = task.result as String

                val policy = ThreadPolicy.Builder()
                    .permitAll().build()
                StrictMode.setThreadPolicy(policy)
                sendApiRequest(token)
            })
    }

    fun sendApiRequest(token: String) {
        val mURL = URL("https://192.168.1.107:8000/registration/")
        var reqParam = "{ \"token\": \"$token\"}"

        with((mURL.openConnection() as HttpsURLConnection).apply{
            sslSocketFactory = createSocketFactory(listOf("TLSv1.2"))
            hostnameVerifier = HostnameVerifier { _, _ -> true }
            readTimeout = 5_000
        }){
            // optional default is GET
            requestMethod = "POST"

            val wr = OutputStreamWriter(getOutputStream());
            wr.write(reqParam);
            wr.flush();

            println("URL : $url")
            println("Response Code : $responseCode")
        }
    }

    // trusts every cert as we're self-signing the django https server
    private fun createSocketFactory(protocols: List<String>) =
        SSLContext.getInstance(protocols[0]).apply {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) = Unit
                override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) = Unit
            })
            init(null, trustAllCerts, SecureRandom())
        }.socketFactory
}