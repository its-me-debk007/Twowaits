package com.example.twowaits.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.twowaits.R
import com.example.twowaits.databinding.FragmentPdfViewerBinding
import com.example.twowaits.util.DownloaderInterface
import com.example.twowaits.util.Utils
import com.example.twowaits.util.downloadImg
import com.example.twowaits.util.hideSystemUi

class PDFViewer : Fragment(R.layout.fragment_pdf_viewer), DownloaderInterface {
    private lateinit var binding: FragmentPdfViewerBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPdfViewerBinding.bind(view)
        val noteName = activity?.intent?.getStringExtra("NOTE NAME")

        val previousPage = activity?.intent?.getStringExtra("PREVIOUS PAGE")

        if (previousPage == "DOWNLOADS") {
            binding.downloadBtn.visibility = View.GONE
            binding.pdf.fromFile(Utils.DOWNLOADED_NOTE).load()
        }

        binding.downloadBtn.setOnClickListener {
            Toast.makeText(context, "Downloading", Toast.LENGTH_SHORT).show()
            binding.downloadBtn.hide()
            downloadImg(
                requireContext(), Utils.PDF_URI,
                "${context?.filesDir}/Downloaded Notes/", "${noteName}.pdf",
                this
            )
        }
    }

    override fun onDownloadError() {
        binding.downloadBtn.show()
    }

    override fun onResume() {
        super.onResume()
        activity?.hideSystemUi(binding.pdf)
    }
}