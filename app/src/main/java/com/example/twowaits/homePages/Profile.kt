package com.example.twowaits.homePages

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.example.twowaits.AuthActivity
import com.example.twowaits.CompanionObjects
import com.example.twowaits.databinding.PleaseWaitDialogBinding
import com.example.twowaits.databinding.ProfileBinding
import com.example.twowaits.viewmodels.ProfileDetailsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class Profile : Fragment() {
    private var _binding: ProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileBinding.inflate(inflater, container, false)

        val viewModel = ViewModelProvider(this)[ProfileDetailsViewModel::class.java]

        val authToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNjQyNDE4Mzc2LCJpYXQiOjE2NDIzMzE5NzYsImp0aSI6ImIzOTlmZTFjZGM2ZjQxMGY5Yzk1ZGVhMzYxOTg5NGVlIiwidXNlcl9pZCI6Nn0.JDrAQVl0A5lcTBy4Np3z4Icr3lHaUr3jZ_AbccUdL24"

        viewModel.profileDetails(authToken)
        viewModel.profileLiveData.observe(viewLifecycleOwner, {
            binding.ProfilePic.load(it.profile_pic) {
                transformations(CircleCropTransformation())
            }
            binding.NameOfUser.text = it.name
//            it.year = when (it.year) {
//                "1" -> "1st Year"
//                "2" -> "2nd Year"
//                "3" -> "3rd Year"
//                else -> "4th Year"
//            }
//            binding.DetailsOfUser.text = "${it.year} | ${it.course} | ${it.branch}"
//            binding.CollegeOfUser.text = it.college
        })
        viewModel.errorLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })

        val chooseImage = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {
            binding.ProfilePic.setImageURI(it)
            val dialog = Dialog(requireContext())
            dialog.setContentView(PleaseWaitDialogBinding.inflate(layoutInflater).root)
            dialog.setCancelable(false)
            dialog.show()
            if (dialog.window != null)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))

            viewModel.uploadProfilePic(it)
            viewModel.uploadImageLiveData.observe(viewLifecycleOwner, { message ->
                if (message == "Uploaded") {
                    Toast.makeText(context, "Successfully uploaded", Toast.LENGTH_SHORT).show()
                    dialog.hide()
                } else {
                    Toast.makeText(
                        context,
                        "$message\nPlease select the image again",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.hide()
                }
            })
        }

        binding.AddPicBtn.setOnClickListener {
            chooseImage.launch("image/*")
        }

        binding.BuyNowBtn.setOnClickListener {
        }

        val viewPagerAdapter = ProfileViewPagerAdapter(childFragmentManager, lifecycle)
        binding.viewPager2.adapter = viewPagerAdapter
        TabLayoutMediator(binding.TabLayout, binding.viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Your Q’s"
                1 -> tab.text = "Wishlist"
            }
        }.attach()

//        viewModel.errorMutableLiveData.observe(viewLifecycleOwner, {
//            if (it == "Token has Expired"){
//                GlobalScope.launch {
//                    val refreshToken = CompanionObjects.readRefreshToken("refreshToken")
//                    viewModel.getNewAccessToken(refreshToken!!)
//                }
//                viewModel.saveRefreshTokenMutableLiveData.observe(viewLifecycleOwner, {
//                    GlobalScope.launch {
//                        CompanionObjects.saveAccessToken("accessToken", it)
//                        CompanionObjects.accessToken = it
//                        viewModel.profileDetails("Bearer $authToken") // Calling function again to get profile Details
//                        viewModel.getQnA()
//                    }
//                })
//            } else {
//                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
//            }
//        })

        binding.Logout.setOnClickListener {
            lifecycleScope.launch {
                CompanionObjects.saveLoginStatus("log_in_status", "false")
            }
            val intent = Intent(context, AuthActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return binding.root
    }

    private fun progressBarDialog() {


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}