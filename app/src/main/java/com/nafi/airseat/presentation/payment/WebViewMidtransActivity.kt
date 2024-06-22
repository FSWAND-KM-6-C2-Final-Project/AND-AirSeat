package com.nafi.airseat.presentation.payment

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.nafi.airseat.R
import com.nafi.airseat.databinding.ActivityWebViewMidtransBinding

class WebViewMidtransActivity : AppCompatActivity() {
    private val binding: ActivityWebViewMidtransBinding by lazy {
        ActivityWebViewMidtransBinding.inflate(layoutInflater)
    }

    private var successDetected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBackWebview.setOnClickListener {
            onBackPressed()
        }

        val url = intent.getStringExtra("URL")
        url?.let {
            openUrlFromWebView(it)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun openUrlFromWebView(url: String) {
        val webView: WebView = findViewById(R.id.myWebView)
        webView.webViewClient =
            object : WebViewClient() {
                val pd = ProgressDialog(this@WebViewMidtransActivity)

                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    request: WebResourceRequest,
                ): Boolean {
                    val requestUrl = request.url.toString()
                    return when {
                        requestUrl.contains("#/success") -> {
                            successDetected = true
                            false
                        }
                        else -> false
                    }
                }

                override fun onPageStarted(
                    view: WebView,
                    url: String,
                    favicon: Bitmap?,
                ) {
                    pd.setMessage(getString(R.string.string_loading))
                    pd.show()
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(
                    view: WebView,
                    url: String,
                ) {
                    pd.dismiss()
                    if (successDetected) {
                        handleSuccessRedirect()
                        successDetected = false
                    }
                    super.onPageFinished(view, url)
                }
            }

        with(webView.settings) {
            loadsImagesAutomatically = true
            javaScriptEnabled = true
        }
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.loadUrl(url)
    }

    private fun handleSuccessRedirect() {
        val flightId = intent.getStringExtra("flightId")
        val intent = Intent(this@WebViewMidtransActivity, PaymentSuccessActivity::class.java)
        intent.putExtra("flightId", flightId)
        startActivity(intent)
        finish()
    }
}
