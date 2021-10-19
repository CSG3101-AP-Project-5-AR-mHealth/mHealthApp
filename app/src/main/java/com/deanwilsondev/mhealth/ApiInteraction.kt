package com.deanwilsondev.mhealth

import java.io.OutputStreamWriter
import java.net.URL
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class ApiInteraction {
    companion object {
        var Ip: String = "127.0.0.1"
        var Port: String = "8000"
        var Status: String = "Not Connected"

        fun sendTokenToApi(token: String) {
            val mURL = URL("https://$Ip:$Port/registration/")
            var reqParam = "{ \"token\": \"$token\"}"

            with((mURL.openConnection() as HttpsURLConnection).apply{
                sslSocketFactory = createSocketFactory(listOf("TLSv1.2"))
                hostnameVerifier = HostnameVerifier { _, _ -> true }
                readTimeout = 2_000
                connectTimeout = 2_000
            }){
                // optional default is GET
                requestMethod = "POST"

                val wr = OutputStreamWriter(getOutputStream());
                wr.write(reqParam);
                wr.flush();

                if(responseCode == 201) {
                    Status = "Connected"
                }
                else {
                    Status = "Server Returned Error"
                }
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
}