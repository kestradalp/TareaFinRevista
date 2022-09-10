package com.example.uteqwebservice.Actividades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import com.example.uteqwebservice.R

class ViewPDFActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pdf)
        try {
            val bundle = intent.extras
            val titulo = bundle?.getString("title").toString()
            val url = bundle?.getString("url").toString()
            val txttitulo: TextView = findViewById(R.id.txttituloviewpdf)
            txttitulo.text = titulo


            var webview: WebView = findViewById(R.id.webview);
            webview.webChromeClient = object : WebChromeClient() {

            }
            webview.webViewClient = object : WebViewClient() {

            }
            webview.settings.javaScriptEnabled = true

            webview.loadUrl(url)
            webview.WebViewTransport()


        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Error: " + e.message, Toast.LENGTH_LONG).show()
        }
    }

}