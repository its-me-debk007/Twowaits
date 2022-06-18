package com.example.twowaits.homePages

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.twowaits.R
import com.example.twowaits.network.dashboardApiCalls.StudentProfileDetailsResponse
import com.example.twowaits.databinding.EnterDetailsStudentBinding
import com.example.twowaits.databinding.PleaseWaitDialog2Binding
import java.util.*

class EditProfileStudent : Fragment(R.layout.enter_details_student) {
    private lateinit var binding: EnterDetailsStudentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = EnterDetailsStudentBinding.bind(view)
        binding.apply {
            val profileDetails = activity?.intent?.extras?.get("profileDetails") as
                    StudentProfileDetailsResponse
            profileDetails.apply {
                Glide.with(requireContext()).load(profile_pic_firebase).into(ProfilePic)
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
//                imgUri = it
            }
            binding.AddPicBtn.setOnClickListener { chooseImage.launch("image/*") }

            binding.submitBtn.setOnClickListener {
                checkValidation()

                val dialog = Dialog(requireContext())
                dialog.setContentView(PleaseWaitDialog2Binding.inflate(layoutInflater).root)
                dialog.setCancelable(false)
                dialog.show()
                if (dialog.window != null)
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(0))

//                viewModel.createStudentProfileDetails(
//                    enterYourName.text.toString().trim(),
//                    enterYourCollege.text.toString().trim(),
//                    autoCompleteTextView2.text.toString().trim(),
//                    autoCompleteTextView3.text.toString().trim(),
//                    autoCompleteTextView4.text.toString().trim(),
//                    autoCompleteTextView.text.toString().trim()[0].toString(),
//                    enterDate.text.toString().trim(),
//                    imgUri!!
//                )
//                viewModel.createStudentProfileLiveData.observe(viewLifecycleOwner) {
//                    if (it == "success") {
//                        activity?.finish()
//                    } else {
//                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
//                        dialog.hide()
//                    }
//                }
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