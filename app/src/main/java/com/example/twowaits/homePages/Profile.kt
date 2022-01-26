package com.example.twowaits.homePages

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.example.twowaits.CompanionObjects
import com.example.twowaits.R
import com.example.twowaits.databinding.PleaseWaitDialogBinding
import com.example.twowaits.databinding.ProfileBinding
import com.example.twowaits.viewmodels.ProfileDetailsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class Profile : Fragment() {
    private var _binding: ProfileBinding? = null
    private val binding get() = _binding!!

    lateinit var name: String
    var account_id = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[ProfileDetailsViewModel::class.java]

//        binding.ShimmerLayout.startShimmer()
        binding.swipeToRefresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_profile_self)
            }, 440)
        }

        if (CompanionObjects.USER == "FACULTY"){
            viewModel.profileDetailsFaculty()
            viewModel.profileFacultyLiveData.observe(viewLifecycleOwner, {
                binding.ProfilePic.load(it.profile_pic) {
                    transformations(CircleCropTransformation())
                }
                name = it.name
                val title: String = when (it.gender) {
                    "M" -> "Sir"
                    "F" -> "Ma'am"
                    else -> "Faculty"
                }
                account_id = it.faculty_account_id
                binding.NameOfUser.text = it.name + " " + title

//            binding.ShimmerLayout.stopShimmer()
//            binding.ShimmerLayout.visibility = View.GONE
//            binding.constraintLayout.visibility = View.VISIBLE
            })
            viewModel.errorFacultyLiveData.observe(viewLifecycleOwner, {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
//            binding.ShimmerLayout.stopShimmer()
//            binding.ShimmerLayout.visibility = View.GONE
//            binding.constraintLayout.visibility = View.VISIBLE
            })
        } else {
            viewModel.profileDetailsStudent()
            viewModel.profileStudentLiveData.observe(viewLifecycleOwner, {
                binding.ProfilePic.load(it.profile_pic_firebase) {
                    transformations(CircleCropTransformation())
                }
                name = it.name
                account_id = it.student_account_id
                binding.NameOfUser.text = it.name
                it.year = when (it.year) {
                    "1" -> "1st Year"
                    "2" -> "2nd Year"
                    "3" -> "3rd Year"
                    else -> "4th Year"
                }
                binding.DetailsOfUser.text = "${it.year} | ${it.course} | ${it.branch}"
                binding.CollegeOfUser.text = it.college

//            binding.ShimmerLayout.stopShimmer()
//            binding.ShimmerLayout.visibility = View.GONE
//            binding.constraintLayout.visibility = View.VISIBLE
            })
            viewModel.errorStudentLiveData.observe(viewLifecycleOwner, {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
//            binding.ShimmerLayout.stopShimmer()
//            binding.ShimmerLayout.visibility = View.GONE
//            binding.constraintLayout.visibility = View.VISIBLE
            })
        }

        val chooseImage = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {
            val dialog = Dialog(requireContext())
            dialog.setContentView(PleaseWaitDialogBinding.inflate(layoutInflater).root)
            dialog.setCancelable(false)
            dialog.show()
            if (dialog.window != null)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))

            viewModel.uploadProfilePic(it, account_id)
            viewModel.uploadImageLiveData.observe(viewLifecycleOwner, { message ->
                if (message.substring(0, 8) == "Uploaded") {
                    val uri = message.substring(8)
                    viewModel.updateProfileDetails(UpdateProfileDetailsBody(name, uri))
                    viewModel.updateProfileDetailsLiveData.observe(viewLifecycleOwner, {
                        Toast.makeText(context, "Successfully uploaded", Toast.LENGTH_SHORT).show()
                    })
                    viewModel.errorUpdateProfileDetailsLiveData.observe(viewLifecycleOwner, { errorMessage ->
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    })
                    binding.ProfilePic.setImageURI(it)
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

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}