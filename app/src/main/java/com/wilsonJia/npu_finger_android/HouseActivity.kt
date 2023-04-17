package com.wilsonJia.npu_finger_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.wilsonJia.npu_finger_android.databinding.ActivityHouseBinding

class HouseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHouseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house)

        binding= ActivityHouseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        runOnUiThread {
            binding.webView.webViewClient = WebViewClient()
            binding.webView.settings.setSupportZoom(true)
            binding.webView.settings.builtInZoomControls = true
            binding.webView.settings.javaScriptEnabled = true
            binding.webView.loadUrl("https://sites.google.com/gms.npu.edu.tw/off-campus-housing")

        }

    }
}