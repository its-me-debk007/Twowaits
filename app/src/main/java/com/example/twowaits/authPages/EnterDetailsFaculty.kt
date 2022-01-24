package com.example.twowaits.authPages

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.twowaits.R
import com.example.twowaits.databinding.EnterDetailsFacultyBinding
import com.example.twowaits.databinding.FirstAuthPageBinding
import com.example.twowaits.databinding.PleaseWaitDialogBinding
import com.example.twowaits.homePages.UpdateProfileDetailsBody
import java.text.SimpleDateFormat
import java.util.*

class EnterDetailsFaculty: Fragment() {
    private var _binding: EnterDetailsFacultyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EnterDetailsFacultyBinding.inflate(inflater, container, false)

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(requireContext(),
            { _, year, month, dayOfMonth ->
                binding.enterDate.text = String.format("%d / %d / %d", dayOfMonth, month + 1, year)
            }, year, month, day)
        binding.enterDate.setOnClickListener {
            datePicker.show()
        }

        val chooseImage = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {
            binding.ProfilePic.setImageURI(it)
//            val dialog = Dialog(requireContext())
//            dialog.setContentView(PleaseWaitDialogBinding.inflate(layoutInflater).root)
//            dialog.setCancelable(false)
//            dialog.show()
//            if (dialog.window != null)
//                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))

//            viewModel.uploadProfilePic(it)
//            viewModel.uploadImageLiveData.observe(viewLifecycleOwner, { message ->
//                if (message == "Uploaded") {
//                    viewModel.updateProfileDetails(UpdateProfileDetailsBody(name))
//                    viewModel.updateProfileDetailsLiveData.observe(viewLifecycleOwner, {
//                        Toast.makeText(context, "Successfully uploaded", Toast.LENGTH_SHORT).show()
//                    })
//                    viewModel.errorUpdateProfileDetailsLiveData.observe(viewLifecycleOwner, { errorMessage ->
//                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
//                    })
                    // Change name of uploaded image in repository
//                    dialog.hide()
//                } else {
//                    Toast.makeText(
//                        context,
//                        "$message\nPlease select the image again",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    dialog.hide()
//                }
//            })
        }

        binding.AddPicBtn.setOnClickListener {
            chooseImage.launch("image/*")
        }

        binding.submitBtn.setOnClickListener {
            when {
                binding.enterYourName.text.isNullOrEmpty() -> {
                    binding.enterName.helperText = "Please enter your name"
                    return@setOnClickListener
                }
                binding.enterYourCollege.text.isNullOrEmpty() -> {
                    binding.enterName.helperText = ""
                    binding.enterCollege.helperText = "Please enter your college name"
                    return@setOnClickListener
                }
                binding.autoCompleteTextView.text.isNullOrEmpty() -> {
                    binding.enterName.helperText = ""
                    binding.enterCollege.helperText = ""
                    binding.genderTextInputLayout.helperText = "Please enter your gender"
                    return@setOnClickListener
                }
                binding.enterYourDepartment.text.isNullOrEmpty() -> {
                    binding.enterName.helperText = ""
                    binding.enterCollege.helperText = ""
                    binding.genderTextInputLayout.helperText = ""
                    binding.enterDepartment.helperText = "Please enter your department"
                }
            }
            binding.enterName.helperText = ""
            binding.enterCollege.helperText = ""
            binding.genderTextInputLayout.helperText = ""
            binding.enterDepartment.helperText = ""

            if (binding.chatToggle.isChecked) {

            } else {

            }
        }

        return  binding.root
    }

    override fun onResume() {
        super.onResume()
        val genderDropdownItems = listOf("Male", "Female", "Other")
        val genderArrayAdapter = ArrayAdapter(requireContext(), R.layout.enter_details_dropdown_item, genderDropdownItems)
        binding.autoCompleteTextView.setAdapter(genderArrayAdapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}