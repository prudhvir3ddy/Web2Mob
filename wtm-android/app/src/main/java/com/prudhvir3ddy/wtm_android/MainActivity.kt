package com.prudhvir3ddy.wtm_android

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams

class MainActivity : AppCompatActivity() {

  private lateinit var webView: WebView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(R.layout.activity_main)

    webView = findViewById(R.id.webview)
    handleInset()

    webView.apply {
      webViewClient = MyWebViewClient()
      settings.javaScriptEnabled = true
      loadUrl("https://x.com/")
    }

    onBackPressedDispatcher.addCallback {
      if (webView.canGoBack()) {
        webView.goBack()
      } else {
        finish()
      }
    }
  }

  private fun handleInset() {
    ViewCompat.setOnApplyWindowInsetsListener(webView) { v, insets ->
      val systemBars =
        insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
      v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
        leftMargin = systemBars.left
        bottomMargin = systemBars.bottom
        rightMargin = systemBars.right
        topMargin = systemBars.top
      }
      insets
    }
  }

  private class MyWebViewClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
      return false
    }
  }
}