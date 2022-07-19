package com.example.twowaits.homePages

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.twowaits.R
import com.example.twowaits.databinding.FragmentPdfViewerBinding
import com.example.twowaits.utils.Utils
import com.example.twowaits.utils.downloadImg

class PDFViewer : Fragment(R.layout.fragment_pdf_viewer) {
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
            binding.downloadBtn.isEnabled = false
            downloadImg(requireContext(), Utils.PDF_URI,
                "${context?.filesDir}/Downloaded Notes/", "${noteName}.pdf"
            )
        }
    }
}