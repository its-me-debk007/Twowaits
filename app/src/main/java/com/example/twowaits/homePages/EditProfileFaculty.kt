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
import com.example.twowaits.network.dashboardApiCalls.FacultyProfileDetailsResponse
import com.example.twowaits.databinding.EnterDetailsFacultyBinding
import com.example.twowaits.databinding.PleaseWaitDialog2Binding
import java.util.*

class EditProfileFaculty : Fragment(R.layout.enter_details_faculty) {
    private lateinit var binding: EnterDetailsFacultyBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = EnterDetailsFacultyBinding.bind(view)
        binding.apply {
            (activity?.intent?.extras?.get("profileDetails") as
                    FacultyProfileDetailsResponse).apply {
                Glide.with(requireActivity()).load(profile_pic_firebase).into(ProfilePic)
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

//            var imgUri: Uri? = null
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
            enterDate.setOnClickListener {
                datePicker.show()
            }

            val chooseImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
                ProfilePic.setImageURI(it)
//                imgUri = it
            }
            AddPicBtn.setOnClickListener {
                chooseImage.launch("image/*")
            }
            submitBtn.setOnClickListener {
                var flag = false
                enterName.helperText = ""
                enterCollege.helperText = ""
                genderTextInputLayout.helperText = ""
                enterDepartment.helperText = ""

                if (enterYourName.text.isNullOrBlank()) {
                    enterName.helperText = "Please enter your name"
                    flag = true
                }
                if (enterYourCollege.text.isNullOrBlank()) {
                    enterCollege.helperText = "Please enter your college"
                    flag = true
                }
                if (autoCompleteTextView.text.isNullOrBlank() || enterDate.text == "DD/MM/YYYY") {
                    genderTextInputLayout.helperText = "Please enter your gender and DOB"
                    flag = true
                }
                if (enterYourDepartment.text.isNullOrBlank()) {
                    enterDepartment.helperText = "Please enter your department"
                    flag = true
                }
                if (flag) return@setOnClickListener

                val dialog = Dialog(requireContext())
                dialog.setContentView(PleaseWaitDialog2Binding.inflate(layoutInflater).root)
                dialog.setCancelable(false)
                dialog.show()
                if (dialog.window != null)
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
//                viewModel.updateFacultyProfileDetails(
//                    enterYourCollege.text.toString().trim(),
//                    enterYourDepartment.text.toString().trim(),
//                    enterDate.text.toString().trim(),
//                    autoCompleteTextView.text.toString().trim()[0].toString(),
//                    enterYourName.text.toString().trim(),
//                    imgUri!!
//                )
//                viewModel.createFacultyProfileLiveData.observe(viewLifecycleOwner) {
//                    if (it == "success") {

//                        }
//                        activity?.finish
//                    } else {
//                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
//                        dialog.hide()
//                    }
//                }
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
}