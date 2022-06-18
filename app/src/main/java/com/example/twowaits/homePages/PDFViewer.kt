package com.example.twowaits.homePages

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.twowaits.R
import com.example.twowaits.databinding.PdfViewerBinding
import com.example.twowaits.homePages.downloads.DownloadedLectures
import com.example.twowaits.homePages.downloads.DownloadedNotes
import com.example.twowaits.utils.Utils

class PDFViewer : Fragment(R.layout.pdf_viewer) {
    private lateinit var binding: PdfViewerBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PdfViewerBinding.bind(view)
        val noteName = activity?.intent?.getStringExtra("NOTE NAME")
        val previousPage = activity?.intent?.getStringExtra("PREVIOUS PAGE")

        if (previousPage == "DOWNLOADS") {
            binding.downloadBtn.visibility = View.GONE
            binding.pdf.fromFile(Utils.DOWNLOADED_NOTE).load()
        }

        binding.downloadBtn.setOnClickListener {
            Toast.makeText(context, "Downloading", Toast.LENGTH_SHORT).show()
            binding.downloadBtn.isEnabled = false
            Utils().downloadImg(
                requireContext(), Utils.PDF_URI,
                "${context?.filesDir}/Downloaded Notes/", "${noteName}.pdf"
            )
        }
    }
}