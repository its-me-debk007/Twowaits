package com.example.twowaits.ui.fragment.auth

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.databinding.EnterDetailsFacultyBinding
import com.example.twowaits.databinding.PleaseWaitDialog2Binding
import com.example.twowaits.ui.activity.home.HomeActivity
import com.example.twowaits.util.Datastore
import com.example.twowaits.util.USER
import com.example.twowaits.viewModel.EnterDetailsViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import java.util.*

@DelicateCoroutinesApi
class EnterDetailsFaculty : Fragment(R.layout.enter_details_faculty) {
    private lateinit var binding: EnterDetailsFacultyBinding
    private val dataStore by lazy { Datastore(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = EnterDetailsFacultyBinding.bind(view)
        val viewModel = ViewModelProvider(this)[EnterDetailsViewModel::class.java]
        var imgUri: Uri? = null
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, _, _, dayOfMonth ->
                binding.enterDate.text = String.format("%d-%d-%d", year, month + 1, dayOfMonth)
            }, year, month, day
        )
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
            var flag = false
            binding.enterName.helperText = ""
            binding.enterCollege.helperText = ""
            binding.genderTextInputLayout.helperText = ""
            binding.enterDepartment.helperText = ""
            if (imgUri == null) {
                Toast.makeText(context, "Please choose your profile pic", Toast.LENGTH_SHORT).show()
                flag = true
            }
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
            if (binding.enterYourDepartment.text.isNullOrBlank()) {
                binding.enterDepartment.helperText = "Please enter your department"
                flag = true
            }
            if (flag) return@setOnClickListener

            val dialog = Dialog(requireContext())
            dialog.setContentView(PleaseWaitDialog2Binding.inflate(layoutInflater).root)
            dialog.setCancelable(false)
            dialog.show()
            if (dialog.window != null)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            viewModel.createFacultyProfileDetails(
                binding.enterYourCollege.text.toString().trim(),
                binding.enterYourDepartment.text.toString().trim(),
                binding.enterDate.text.toString().trim(),
                binding.autoCompleteTextView.text.toString().trim()[0].toString(),
                binding.enterYourName.text.toString().trim(),
                imgUri!!
            )
            viewModel.createFacultyProfileLiveData.observe(viewLifecycleOwner) {
                if (it == "success") {
                    USER = "FACULTY"
                    lifecycleScope.launch {
                        val userEmail = EnterDetailsFacultyArgs.fromBundle(
                            requireArguments()
                        ).userEmail
                        dataStore.saveUserDetails("email", userEmail)
                    }
                    val intent = Intent(activity, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    dialog.hide()
                }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_enterDetailsFaculty_to_chooseYourRole)
            }
        })
    }
}