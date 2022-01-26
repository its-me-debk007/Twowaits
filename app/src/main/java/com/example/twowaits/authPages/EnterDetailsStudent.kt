package com.example.twowaits.authPages

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.twowaits.CompanionObjects
import com.example.twowaits.HomeActivity
import com.example.twowaits.R
import com.example.twowaits.databinding.EnterDetailsFacultyBinding
import com.example.twowaits.databinding.EnterDetailsStudentBinding
import com.example.twowaits.databinding.PleaseWaitDialog2Binding
import com.example.twowaits.databinding.PleaseWaitDialogBinding
import com.example.twowaits.repository.authRepositories.CreateProfileRepository
import com.example.twowaits.viewmodels.EnterDetailsViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import java.util.*

@DelicateCoroutinesApi
class EnterDetailsStudent: Fragment() {
    private var _binding: EnterDetailsStudentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EnterDetailsStudentBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[EnterDetailsViewModel::class.java]
        var imgUri: Uri? = null
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(requireContext(),
            { _, year, month, dayOfMonth ->
                binding.enterDate.text = String.format("%d-%d-%d", year, month + 1, dayOfMonth)
            }, year, month, day)
        binding.enterDate.setOnClickListener {
            datePicker.show()
        }

        val chooseImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            binding.ProfilePic.setImageURI(it)
            imgUri = it
        }
        binding.AddPicBtn.setOnClickListener {
            chooseImage.launch("image/*")
        }

        binding.submitBtn.setOnClickListener {
            when {
                imgUri.toString().trim() == "null" -> {
                    Toast.makeText(context, "Please select a profile pic", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                binding.enterYourName.text.isNullOrEmpty() -> {
                    binding.enterName.helperText = "Please enter your name"
                    return@setOnClickListener
                }
                binding.autoCompleteTextView3.text.isNullOrEmpty() -> {
                    binding.enterName.helperText = ""
                    binding.enterBranch.helperText = "Please select your branch"
                    return@setOnClickListener
                }
                binding.autoCompleteTextView.text.isNullOrEmpty() -> {
                    binding.enterName.helperText = ""
                    binding.enterBranch.helperText = ""
                    binding.genderTextInputLayout.helperText = "Please enter your gender and DOB"
                    return@setOnClickListener
                }
                binding.enterYourCollege.text.isNullOrEmpty() -> {
                    binding.enterName.helperText = ""
                    binding.enterBranch.helperText = ""
                    binding.genderTextInputLayout.helperText = ""
                    binding.enterCollege.helperText = "Please enter your college name"
                    return@setOnClickListener
                }
                binding.autoCompleteTextView2.text.isNullOrEmpty() -> {
                    binding.enterName.helperText = ""
                    binding.enterBranch.helperText = ""
                    binding.genderTextInputLayout.helperText = ""
                    binding.enterCollege.helperText = ""
                    binding.courseTextInputLayout.helperText = "Please select your course"
                    return@setOnClickListener
                }
                binding.autoCompleteTextView4.text.isNullOrEmpty() -> {
                    binding.enterName.helperText = ""
                    binding.enterBranch.helperText = ""
                    binding.genderTextInputLayout.helperText = ""
                    binding.enterCollege.helperText = ""
                    binding.courseTextInputLayout.helperText = ""
                    binding.yearTextInputLayout.helperText = "Please select your current year"
                    return@setOnClickListener
                }
            }
            binding.enterName.helperText = ""
            binding.enterBranch.helperText = ""
            binding.genderTextInputLayout.helperText = ""
            binding.enterCollege.helperText = ""
            binding.courseTextInputLayout.helperText = ""
            binding.yearTextInputLayout.helperText = ""

            val dialog = Dialog(requireContext())
            dialog.setContentView(PleaseWaitDialog2Binding.inflate(layoutInflater).root)
            dialog.setCancelable(false)
            dialog.show()
            if (dialog.window != null)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))

            viewModel.createStudentProfileDetails(
                binding.enterYourName.text.toString().trim(),
                binding.enterYourCollege.text.toString().trim(),
                binding.autoCompleteTextView2.text.toString().trim(),
                binding.autoCompleteTextView3.text.toString().trim(),
                binding.autoCompleteTextView4.text.toString().trim(),
                binding.autoCompleteTextView.text.toString().trim()[0].toString(),
                binding.enterDate.text.toString().trim(),
                imgUri!!
            )
            viewModel.createStudentProfileLiveData.observe(viewLifecycleOwner, {
                if (it == "success") {
                    val intent = Intent(activity, HomeActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                } else {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    dialog.hide()
                }
            })
        }
        return  binding.root
    }

    override fun onResume() {
        super.onResume()
        val genderDropdownItems = listOf("Male", "Female", "Other")
        val genderArrayAdapter = ArrayAdapter(requireContext(), R.layout.enter_details_dropdown_item, genderDropdownItems)
        binding.autoCompleteTextView.setAdapter(genderArrayAdapter)
        val courseDropDownItems = listOf("BTECH", "MTECH", "BCA", "MCA", "MBA", "BBA", "BA", "MA")
        val courseArrayAdapter = ArrayAdapter(requireContext(), R.layout.enter_details_dropdown_item, courseDropDownItems)
        binding.autoCompleteTextView2.setAdapter(courseArrayAdapter)
        val branchDropDownItems = listOf("CS", "CS&IT", "IT", "ME", "CE", "EE", "Humanities", "Others")
        val branchArrayAdapter = ArrayAdapter(requireContext(), R.layout.enter_details_dropdown_item, branchDropDownItems)
        binding.autoCompleteTextView3.setAdapter(branchArrayAdapter)
        val yearDropDownItems = listOf("1", "2", "3", "4")
        val yearArrayAdapter = ArrayAdapter(requireContext(), R.layout.enter_details_dropdown_item, yearDropDownItems)
        binding.autoCompleteTextView4.setAdapter(yearArrayAdapter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_enterDetailsStudent_to_chooseYourRole)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}