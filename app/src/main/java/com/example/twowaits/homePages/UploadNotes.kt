package com.example.twowaits.homePages

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.databinding.PleaseWaitDialogBinding
import com.example.twowaits.databinding.UploadNotesBinding
import com.example.twowaits.viewmodels.HomePageViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class UploadNotes : Fragment() {
    private var _binding: UploadNotesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UploadNotesBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[HomePageViewModel::class.java]
        var pdfUri: Uri? = null
        val choosePDF = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {
            binding.thumbnail.setImageURI(it)
            pdfUri = it
            if (pdfUri != null) binding.selectPDF.text = "Selected ✔"
        }
        binding.selectPDF.setOnClickListener {
            choosePDF.launch("application/pdf")
        }
        binding.upload.setOnClickListener {
            when {
                binding.title.text.isNullOrEmpty() -> {
                    binding.titleOfNote.helperText = "Please enter the title"
                    return@setOnClickListener
                }
                binding.description.text.isNullOrEmpty() -> {
                    binding.titleOfNote.helperText = ""
                    binding.descriptionLayout.helperText = "Please add the description"
                    return@setOnClickListener
                }
                binding.selectPDF.text != "Selected ✔" -> {
                    binding.titleOfNote.helperText = ""
                    binding.descriptionLayout.helperText = ""
                    Toast.makeText(context, "Please select a file", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            binding.titleOfNote.helperText = ""
            binding.descriptionLayout.helperText = ""
            binding.upload.isEnabled = false
            val dialog = Dialog(requireContext())
            dialog.setContentView(PleaseWaitDialogBinding.inflate(layoutInflater).root)
            dialog.setCancelable(false)
            dialog.show()
            if (dialog.window != null)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))

            viewModel.uploadNote(pdfUri!!,
                UploadNotePartialBody(binding.description.text.toString().trim(), binding.title.text.toString().trim()))
            viewModel.uploadPDF.observe(viewLifecycleOwner, { message ->
                if (message == "success") {
                    Toast.makeText(context, "Successfully uploaded", Toast.LENGTH_SHORT).show()
                    binding.upload.isEnabled = true
                    dialog.hide()
                    findNavController().navigate(R.id.action_uploadNotes2_to_explore)
                } else {
                    Toast.makeText(
                        context,
                        "$message\nPlease try again",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.upload.isEnabled = true
                    dialog.hide()
                }
            })
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}