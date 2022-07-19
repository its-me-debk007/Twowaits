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
import com.example.twowaits.databinding.EnterDetailsStudentBinding
import com.example.twowaits.model.ProfileDetailsExcludingId
import com.example.twowaits.network.dashboardApiCalls.StudentProfileDetailsResponse
import com.example.twowaits.viewmodel.ProfileViewModel
import java.util.*

class EditProfileStudent : Fragment(R.layout.enter_details_student) {
    private lateinit var binding: EnterDetailsStudentBinding
    private val viewModel by lazy { ViewModelProvider(this)[ProfileViewModel::class.java] }
    private lateinit var data: StudentProfileDetailsResponse
    private var imgUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = EnterDetailsStudentBinding.bind(view).apply {
            (activity?.intent?.extras?.get("profileDetails") as
                    StudentProfileDetailsResponse).apply {
                data = this
                Glide.with(requireContext())
                    .load(profile_pic_firebase)
                    .into(ProfilePic)
                enterYourName.setText(name)
                autoCompleteTextView3.setText(branch)
                autoCompleteTextView.setText(
                    when (gender) {
                        "M" -> "Male"
                        "F" -> "Female"
                        else -> "Other"
                    }
                )
                enterDate.text = dob
                enterYourCollege.setText(college)
                autoCompleteTextView2.setText(course)
                autoCompleteTextView4.setText(year)
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
            binding.AddPicBtn.setOnClickListener { chooseImage.launch("image/*") }

            binding.submitBtn.setOnClickListener {
                checkValidation()

                imgUri?.let {
                    viewModel.uploadPicFirebase(it, data.student_account_id)
                    viewModel.firebaseLiveData.observe(viewLifecycleOwner) { message ->
                        if (message.substring(0, 8) == "Uploaded") {
                            val uri = message.substring(8)
                            updateProfile(uri)
                        } else Toast.makeText(context,
                            "$message\nPlease try again",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                } ?: updateProfile(data.profile_pic_firebase)
            }
        }
    }

    private fun checkValidation(): Boolean {
        var flag = false
        binding.enterName.helperText = ""
        binding.enterBranch.helperText = ""
        binding.genderTextInputLayout.helperText = ""
        binding.enterCollege.helperText = ""
        binding.courseTextInputLayout.helperText = ""
        binding.yearTextInputLayout.helperText = ""
//        if (imgUri == null) {
//            Toast.makeText(context, "Please choose your profile pic", Toast.LENGTH_SHORT).show()
//            flag = true
//        }
        if (binding.enterYourName.text.isNullOrBlank()) {
            binding.enterName.helperText = "Please enter your name"
            flag = true
        }
        if (binding.enterYourCollege.text.isNullOrBlank()) {
            binding.enterCollege.helperText = "Please enter your college"
            flag = true
        }
        if (binding.autoCompleteTextView.text.isNullOrBlank() || binding.enterDate.text == "DD/MM/YYYY") {
            binding.genderTextInputLayout.helperText = "Please enter your gender and DOB"
            flag = true
        }
        if (binding.enterYourCollege.text.isNullOrBlank()) {
            binding.enterCollege.helperText = "Please enter your college"
            flag = true
        }
        if (binding.autoCompleteTextView2.text.isNullOrBlank()) {
            binding.courseTextInputLayout.helperText = "Please enter your course"
            flag = true
        }
        if (binding.autoCompleteTextView4.text.isNullOrBlank()) {
            binding.yearTextInputLayout.helperText = "Please enter your college year"
            flag = true
        }
        if (binding.autoCompleteTextView3.text.isNullOrBlank()) {
            binding.enterBranch.helperText = "Please enter your branch"
            flag = true
        }

        if (flag) return true
        return false
    }

    private fun updateProfile(uri: String) {
        binding.apply {
            viewModel.updateProfile(
                ProfileDetailsExcludingId(
                    enterYourName.text?.trim().toString(),
                    enterYourCollege.text?.trim().toString(),
                    gender = autoCompleteTextView.text.toString(),
                    dob = enterDate.text.toString(),
                    profile_pic_firebase = uri,
                    course = autoCompleteTextView2.text.toString(),
                    year = autoCompleteTextView4.text.toString(),
                    branch = autoCompleteTextView3.text.toString()
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

    override fun onResume() {
        super.onResume()
        val genderDropdownItems = listOf("Male", "Female", "Other")
        val genderArrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.enter_details_dropdown_item,
            genderDropdownItems
        )
        binding.autoCompleteTextView.setAdapter(genderArrayAdapter)
        val courseDropDownItems = listOf("BTECH", "MTECH", "BCA", "MCA", "MBA", "BBA", "BA", "MA")
        val courseArrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.enter_details_dropdown_item,
            courseDropDownItems
        )
        binding.autoCompleteTextView2.setAdapter(courseArrayAdapter)
        val branchDropDownItems =
            listOf("CS", "CS&IT", "IT", "ME", "CE", "EE", "Humanities", "Others")
        val branchArrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.enter_details_dropdown_item,
            branchDropDownItems
        )
        binding.autoCompleteTextView3.setAdapter(branchArrayAdapter)
        val yearDropDownItems = listOf("1", "2", "3", "4")
        val yearArrayAdapter =
            ArrayAdapter(requireContext(), R.layout.enter_details_dropdown_item, yearDropDownItems)
        binding.autoCompleteTextView4.setAdapter(yearArrayAdapter)
    }
}