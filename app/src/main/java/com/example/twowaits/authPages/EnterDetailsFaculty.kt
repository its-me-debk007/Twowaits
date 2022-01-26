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
import androidx.navigation.fragment.findNavController
import com.example.twowaits.HomeActivity
import com.example.twowaits.R
import com.example.twowaits.databinding.EnterDetailsFacultyBinding
import com.example.twowaits.databinding.PleaseWaitDialog2Binding
import com.example.twowaits.databinding.PleaseWaitDialogBinding
import com.example.twowaits.viewmodels.EnterDetailsViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import java.util.*

@DelicateCoroutinesApi
class EnterDetailsFaculty : Fragment() {
    private var _binding: EnterDetailsFacultyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EnterDetailsFacultyBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[EnterDetailsViewModel::class.java]
        var imgUri: Uri? = null
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(requireContext(),
            { _, year, month, dayOfMonth ->
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
            when {
                binding.enterYourName.text.toString().trim().isEmpty() -> {
                    binding.enterName.helperText = "Please enter your name"
                    return@setOnClickListener
                }
                binding.enterYourCollege.text.toString().trim().isEmpty() -> {
                    binding.enterName.helperText = ""
                    binding.enterCollege.helperText = "Please enter your college name"
                    return@setOnClickListener
                }
                binding.autoCompleteTextView.text.toString().trim()
                    .isEmpty() || binding.enterDate.text.toString().trim().isEmpty() -> {
                    binding.enterName.helperText = ""
                    binding.enterCollege.helperText = ""
                    binding.genderTextInputLayout.helperText = "Please enter your gender and DOB"
                    return@setOnClickListener
                }
                binding.enterYourDepartment.text.toString().trim().isEmpty() -> {
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

            val dialog = Dialog(requireContext())
            dialog.setContentView(PleaseWaitDialog2Binding.inflate(layoutInflater).root)
            dialog.setCancelable(false)
            dialog.show()
            if (dialog.window != null)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))

            viewModel.createFacultyProfileDetails(
                binding.enterYourName.text.toString().trim(),
                binding.enterYourDepartment.text.toString().trim(),
                binding.enterYourCollege.text.toString().trim(),
                binding.autoCompleteTextView.text.toString().trim()[0].toString(),
                binding.enterDate.text.toString().trim(),
                imgUri!!
            )
            viewModel.createFacultyProfileLiveData.observe(viewLifecycleOwner, {
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
        return binding.root
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_enterDetailsFaculty_to_chooseYourRole)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}