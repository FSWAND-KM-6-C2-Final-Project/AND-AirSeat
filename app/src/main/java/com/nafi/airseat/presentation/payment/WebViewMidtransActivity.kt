package com.nafi.airseat.presentation.payment

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.nafi.airseat.R
import com.nafi.airseat.databinding.ActivityWebViewMidtransBinding
import com.nafi.airseat.presentation.main.MainActivity

class WebViewMidtransActivity : AppCompatActivity() {
    private val binding: ActivityWebViewMidtransBinding by lazy {
        ActivityWebViewMidtransBinding.inflate(layoutInflater)
    }

    private var redirectHandled = false
    private var lastUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val url = intent.getStringExtra("URL")
        url?.let {
            openUrlFromWebView(it)
        }

        binding.mbHome.setOnClickListener {
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        startActivity(
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            },
        )
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
                    if (requestUrl.contains("airseat.netlify.app/payment-success") && !redirectHandled) {
                        handleSuccessRedirect(requestUrl)
                        redirectHandled = true
                        return true
                    }
                    return false
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

    override fun onResume() {
        super.onResume()
        lastUrl?.let {
            if (it.contains("airseat.netlify.app/payment-success") && !redirectHandled) {
                handleSuccessRedirect(it)
            }
        }
    }

    private fun handleSuccessRedirect(url: String) {
        val uri = Uri.parse(url)
        val statusCode = uri.getQueryParameter("status_code")
        val transactionStatus = uri.getQueryParameter("transaction_status")

        if (statusCode == "200" && transactionStatus == "settlement") {
            val intent = Intent(this@WebViewMidtransActivity, PaymentSuccessActivity::class.java)
            startActivity(intent)
        }
    }
}
