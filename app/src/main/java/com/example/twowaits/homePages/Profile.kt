package com.example.twowaits.homePages

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.example.twowaits.Data
import com.example.twowaits.PaymentActivity
import com.example.twowaits.R
import com.example.twowaits.databinding.PleaseWaitDialogBinding
import com.example.twowaits.databinding.ProfileBinding
import com.example.twowaits.viewmodels.ProfileDetailsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.razorpay.Checkout
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class Profile : Fragment() {
    private var _binding: ProfileBinding? = null
    private val binding get() = _binding!!
    lateinit var name: String
    private var account_id = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[ProfileDetailsViewModel::class.java]
        var price: Int
        var imgUri: Uri? = null

        binding.swipeToRefresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_profile_self)
            }, 440)
        }

        price = 99
        binding.cardView.strokeColor = Color.parseColor("#804D37")
        binding.cardView.strokeWidth = 6

        if (Data.USER == "FACULTY") {
            viewModel.profileDetailsFaculty()
            viewModel.profileFacultyLiveData.observe(viewLifecycleOwner) {
                binding.ProfilePic.load(it.profile_pic_firebase) {
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
            }
            viewModel.errorFacultyLiveData.observe(viewLifecycleOwner) {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            }
        } else {
            viewModel.profileDetailsStudent()
            viewModel.profileStudentLiveData.observe(viewLifecycleOwner) {
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
            }
            viewModel.errorStudentLiveData.observe(viewLifecycleOwner) {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            }
        }

        val chooseImage = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {
            imgUri = it
            if (imgUri != null) {
                val dialog = Dialog(requireContext())
                dialog.setContentView(PleaseWaitDialogBinding.inflate(layoutInflater).root)
                dialog.setCancelable(false)
                dialog.show()
                if (dialog.window != null)
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(0))

                viewModel.uploadProfilePic(imgUri!!, account_id)
                viewModel.uploadImageLiveData.observe(viewLifecycleOwner) { message ->
                    if (message.substring(0, 8) == "Uploaded") {
                        val uri = message.substring(8)
                        viewModel.updateProfileDetails(UpdateProfileDetailsBody(name, uri))
                        viewModel.updateProfileDetailsLiveData.observe(viewLifecycleOwner) {
                            Toast.makeText(context, "Successfully uploaded", Toast.LENGTH_SHORT)
                                .show()
                        }
                        viewModel.errorUpdateProfileDetailsLiveData.observe(
                            viewLifecycleOwner
                        ) { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                        binding.ProfilePic.setImageURI(imgUri!!)
                        dialog.hide()
                    } else {
                        Toast.makeText(
                            context,
                            "$message\nPlease select the image again",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialog.hide()
                    }
                }
            }
        }

        binding.AddPicBtn.setOnClickListener {
            chooseImage.launch("image/*")
        }

        binding.cardView.setOnClickListener {
            price = 99
            binding.cardView.strokeColor = Color.parseColor("#804D37")
            binding.cardView.strokeWidth = 6
            binding.cardView.cardElevation = 15F
            binding.cardView2.strokeWidth = 0
            binding.cardView2.cardElevation = 0F
        }
        binding.cardView2.setOnClickListener {
            price = 199
            binding.cardView2.strokeColor = Color.parseColor("#804D37")
            binding.cardView2.strokeWidth = 6
            binding.cardView2.cardElevation = 15F
            binding.cardView.strokeWidth = 0
            binding.cardView.cardElevation = 0F
        }

        Checkout.preload(context)
        binding.BuyNowBtn.setOnClickListener {
            try {
                val intent = Intent(activity, PaymentActivity::class.java)
                intent.putExtra("name", name)
                intent.putExtra("price", price)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Some error has occurred\nPlease try again!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val viewPagerAdapter = ProfileViewPagerAdapter(childFragmentManager, lifecycle)
        binding.viewPager2.adapter = viewPagerAdapter
        TabLayoutMediator(binding.TabLayout, binding.viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Your Q’s"
                1 -> tab.text = "Wishlist"
            }
        }.attach()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_profile_to_homePage)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}