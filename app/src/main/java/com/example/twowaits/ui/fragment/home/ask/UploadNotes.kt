package com.example.twowaits.ui.fragment.home.ask

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.twowaits.R
import com.example.twowaits.databinding.FragmentUploadNoteBinding
import com.example.twowaits.databinding.PleaseWaitDialogBinding
import com.example.twowaits.model.home.UploadNotePartialBody
import com.example.twowaits.viewModel.HomePageViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class UploadNotes : Fragment(R.layout.fragment_upload_note) {
    private lateinit var binding: FragmentUploadNoteBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUploadNoteBinding.bind(view)
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

            viewModel.uploadNote(
                pdfUri!!,
                UploadNotePartialBody(
                    binding.description.text.toString().trim(),
                    binding.title.text.toString().trim()
                )
            )
            viewModel.uploadPDF.observe(viewLifecycleOwner) { message ->
                if (message == "success") {
                    Toast.makeText(context, "Successfully uploaded", Toast.LENGTH_SHORT).show()
                    activity?.finish()
                } else {
                    Toast.makeText(
                        context,
                        "$message\nPlease try again",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.upload.isEnabled = true
                    dialog.hide()
                }
            }
        }
    }
}