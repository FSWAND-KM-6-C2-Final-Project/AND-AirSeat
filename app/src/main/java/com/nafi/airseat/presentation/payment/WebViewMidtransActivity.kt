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
import com.nafi.airseat.presentation.flightdetail.FlightDetailActivity

class WebViewMidtransActivity : AppCompatActivity() {
    private val binding: ActivityWebViewMidtransBinding by lazy {
        ActivityWebViewMidtransBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnCloseSnap.setOnClickListener {
            val intent = Intent(this, FlightDetailActivity::class.java)
            startActivity(intent)
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
                        requestUrl.contains("gojek://") ||
                            requestUrl.contains("shopeeid://") ||
                            requestUrl.contains("//wsa.wallet.airpay.co.id/") ||
                            requestUrl.contains("/gopay/partner/") ||
                            requestUrl.contains("/shopeepay/") -> {
                            val intent = Intent(Intent.ACTION_VIEW, request.url)
                            startActivity(intent)
                            true
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
}
