package com.example.twowaits.homePages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.twowaits.databinding.PdfViewerBinding

class PDFViewer: Fragment() {
    private var _binding: PdfViewerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PdfViewerBinding.inflate(inflater, container, false)

        binding.pdf.fromAsset("DataStructures.pdf").load()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}