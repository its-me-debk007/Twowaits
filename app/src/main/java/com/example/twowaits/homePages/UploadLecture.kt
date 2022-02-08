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
import com.example.twowaits.databinding.UploadLectureBinding
import com.example.twowaits.viewmodels.HomePageViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class UploadLecture: Fragment() {
    private var _binding: UploadLectureBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UploadLectureBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[HomePageViewModel::class.java]
        var videoUri: Uri? = null
        val chooseLecture = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {
            binding.thumbnail.setImageURI(it)
            videoUri = it
            if (videoUri != null) binding.selectLecture.text = "Selected ✔"
        }
        binding.selectLecture.setOnClickListener {
            //Uploading to Firebase only in mp4
            chooseLecture.launch("video/*")
        }
        binding.upload.setOnClickListener {
            when {
                binding.title.text.isNullOrEmpty() -> {
                    binding.titleOfLecture.helperText = "Please enter the title"
                    return@setOnClickListener
                }
                binding.description.text.isNullOrEmpty() -> {
                    binding.titleOfLecture.helperText = ""
                    binding.descriptionLayout.helperText = "Please add the description"
                    return@setOnClickListener
                }
                binding.selectLecture.text != "Selected ✔" -> {
                    binding.titleOfLecture.helperText = ""
                    binding.descriptionLayout.helperText = ""
                    Toast.makeText(context, "Please select a file", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            binding.titleOfLecture.helperText = ""
            binding.descriptionLayout.helperText = ""
            binding.upload.isEnabled = false
            val dialog = Dialog(requireContext())
            dialog.setContentView(PleaseWaitDialogBinding.inflate(layoutInflater).root)
            dialog.setCancelable(false)
            dialog.show()
            if (dialog.window != null)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))

            viewModel.uploadLecture(binding.title.text.toString().trim(),
                binding.description.text.toString().trim(), videoUri!!)
            viewModel.uploadLectureData.observe(viewLifecycleOwner) { message ->
                if (message == "success") {
                    Toast.makeText(context, "Successfully uploaded", Toast.LENGTH_SHORT).show()
                    binding.upload.isEnabled = true
                    dialog.hide()
                    findNavController().navigate(R.id.action_uploadLecture_to_explore)
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

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}