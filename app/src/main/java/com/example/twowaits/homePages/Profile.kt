package com.example.twowaits.homePages

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.twowaits.databinding.ProfileBinding
import coil.load
import coil.transform.CircleCropTransformation
import com.example.twowaits.R
import com.example.twowaits.repository.dashboardRepositories.ProfileRepository
import com.example.twowaits.viewmodels.dashboardViewmodels.ProfileDetailsViewModel

class Profile : Fragment() {
    private var _binding: ProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileBinding.inflate(inflater, container, false)

        val viewModel = ViewModelProvider(this)[ProfileDetailsViewModel::class.java]
        val authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNjQxNTM3ODE3LCJpYXQiOjE2NDE0NTE0MTcsImp0aSI6IjdkM2IwNTM1MjRmNDQzYmFiNzczMDU5MmVlNDg4YTI2IiwidXNlcl9pZCI6NH0.bBTE3kMZn-3FKZR4YcHg1ZH3Lw76lWjzKyn4ozL6bAw"
        viewModel.profileDetails("Bearer $authToken")
        viewModel.profileMutableLiveData.observe(viewLifecycleOwner, {
            binding.ProfilePic.load(it.profile_pic){
                transformations(CircleCropTransformation())
            }
            binding.NameOfUser.text = it.name
            it.year = when(it.year){
                "1" -> "1st Year"
                "2" -> "2nd Year"
                "3" -> "3rd Year"
                "4" -> "4th Year"
                else -> ""
            }
            binding.DetailsOfUser.text = "${it.year} | ${it.course} | ${it.branch}"
            binding.CollegeOfUser.text = it.college
        })
    viewModel.errorMutableLiveData.observe(viewLifecycleOwner, {
        Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
    })

        val chooseImage = registerForActivityResult(ActivityResultContracts.GetContent(),
        ActivityResultCallback {
            binding.ProfilePic.setImageURI(it)
        })

        binding.AddPicBtn.setOnClickListener {
            chooseImage.launch("image/*")
            viewModel.uploadProfilePic()
        }

        binding.BuyNowBtn.setOnClickListener {
        }

        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}