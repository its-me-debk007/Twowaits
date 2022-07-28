package com.example.twowaits.ui.fragment.editProfile

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.twowaits.R
import com.example.twowaits.databinding.EnterDetailsFacultyBinding
import com.example.twowaits.model.ProfileDetailsExcludingId
import com.example.twowaits.network.dashboardApiCalls.FacultyProfileDetailsResponse
import com.example.twowaits.viewModel.ProfileViewModel
import java.util.*

class EditProfileFaculty : Fragment(R.layout.enter_details_faculty) {
    private lateinit var binding: EnterDetailsFacultyBinding
    private val viewModel by lazy { ViewModelProvider(this)[ProfileViewModel::class.java] }
    private lateinit var data: FacultyProfileDetailsResponse
    private var imgUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = EnterDetailsFacultyBinding.bind(view).apply {
            (activity?.intent?.getParcelableExtra<FacultyProfileDetailsResponse>("profileDetails")).apply {
                data = this!!
                Glide.with(requireActivity())
                    .load(profile_pic_firebase)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(ProfilePic)
                enterYourName.setText(name)
                enterYourCollege.setText(college)
                autoCompleteTextView.setText(
                    when (gender) {
                        "M" -> "Male"
                        "F" -> "Female"
                        else -> "Other"
                    }
                )
                enterDate.text = dob
                enterYourDepartment.setText(department)
            }

            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, _, _, dayOfMonth ->
                    enterDate.text = String.format("%d-%d-%d", year, month + 1, dayOfMonth)
                }, year, month, day
            )
            enterDate.setOnClickListener { datePicker.show() }

            val chooseImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
                ProfilePic.setImageURI(it)
                imgUri = it
            }
            AddPicBtn.setOnClickListener { chooseImage.launch("image/*") }
            submitBtn.setOnClickListener {
                var flag = false
                enterName.helperText = ""
                enterCollege.helperText = ""
                genderTextInputLayout.helperText = ""
                enterDepartment.helperText = ""

                if (enterYourName.text.isNullOrBlank()) {
                    enterName.helperText = "Enter name"
                    flag = true
                }
                if (enterYourCollege.text.isNullOrBlank()) {
                    enterCollege.helperText = "Enter college"
                    flag = true
                }
                if (autoCompleteTextView.text.isNullOrBlank() || enterDate.text == "DD/MM/YYYY") {
                    genderTextInputLayout.helperText = "Enter gender and DOB"
                    flag = true
                }
                if (enterYourDepartment.text.isNullOrBlank()) {
                    enterDepartment.helperText = "Enter department"
                    flag = true
                }
                if (flag) return@setOnClickListener

                submitBtn.isEnabled = false
                submitBtn.text = ""
                progressBar.show()
                imgUri?.let {
                    viewModel.uploadPicFirebase(it, data.faculty_account_id)
                    viewModel.firebaseLiveData.observe(viewLifecycleOwner) { message ->
                        if (message.substring(0, 8) == "Uploaded") {
                            val uri = message.substring(8)
                            updateProfile(uri)
                        } else {
                            Toast.makeText(
                                context, "$message\nPlease try again",
                                Toast.LENGTH_SHORT
                            ).show()
                            submitBtn.isEnabled = true
                            submitBtn.text = "Submit"
                            progressBar.hide()
                        }
                    }
                } ?: updateProfile(data.profile_pic_firebase!!)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val genderDropdownItems = listOf("Male", "Female", "Other")
        val genderArrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.enter_details_dropdown_item,
            genderDropdownItems
        )
        val departmentDropDownItems =
            listOf("CS", "CS&IT", "IT", "ME", "CE", "EE", "Humanities", "Others")
        val departmentArrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.enter_details_dropdown_item,
            departmentDropDownItems
        )
        binding.autoCompleteTextView.setAdapter(genderArrayAdapter)
        binding.enterYourDepartment.setAdapter(departmentArrayAdapter)
    }

    private fun updateProfile(uri: String) {
        binding.apply {
            viewModel.updateProfile(
                ProfileDetailsExcludingId(
                    enterYourName.text?.trim().toString(),
                    enterYourCollege.text?.trim().toString(),
                    enterYourDepartment.text.toString(),
                    gender = autoCompleteTextView.text[0].toString(),
                    dob = enterDate.text.toString(),
                    profile_pic_firebase = uri
                )
            )
            viewModel.updateProfileLiveData.observe(viewLifecycleOwner) {
                Toast.makeText(
                    context, if (it == "success") "Updated successfully" else it,
                    Toast.LENGTH_SHORT
                ).show()

                if (it == "success") activity?.finish()
            }
        }
    }
}