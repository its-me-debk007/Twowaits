package com.example.twowaits.homePages

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.twowaits.R
import com.example.twowaits.databinding.FragmentWebviewBinding

class WebViewPDFViewer : Fragment(R.layout.fragment_webview) {
    private lateinit var binding: FragmentWebviewBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWebviewBinding.bind(view)
        binding.apply {
            val pdf = "http://www.africau.edu/images/default/sample.pdf"
            binding.onlineViewer.settings.javaScriptEnabled = true
            binding.onlineViewer.loadUrl("https://docs.google.com/gview?embedded=true&url=$pdf")

            binding.onlineViewer.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {}
            }
        }
    }
}