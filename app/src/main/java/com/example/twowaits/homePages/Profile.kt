package com.example.twowaits.homePages

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.twowaits.ui.activities.home.AskActivity
import com.example.twowaits.utils.Utils
import com.example.twowaits.PaymentActivity
import com.example.twowaits.R
import com.example.twowaits.network.dashboardApiCalls.FacultyProfileDetailsResponse
import com.example.twowaits.network.dashboardApiCalls.StudentProfileDetailsResponse
import com.example.twowaits.databinding.PleaseWaitDialogBinding
import com.example.twowaits.databinding.ProfileBinding
import com.example.twowaits.viewmodels.ProfileDetailsViewModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayoutMediator
import com.razorpay.Checkout
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class Profile : Fragment(R.layout.profile) {
    private lateinit var binding: ProfileBinding
    lateinit var name: String
    private var accountId = 0
    private lateinit var profileDetailsStudent: StudentProfileDetailsResponse
    private lateinit var profileDetailsFaculty: FacultyProfileDetailsResponse

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProfileBinding.bind(view)
        val viewModel = ViewModelProvider(this)[ProfileDetailsViewModel::class.java]
        var price: Int

        binding.swipeToRefresh.setColorSchemeColors(Color.parseColor("#804D37"))
        binding.swipeToRefresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_profile_self)
            }, 440)
        }

        price = 199
        binding.cardView2.strokeColor = Color.parseColor("#804D37")
        binding.cardView2.strokeWidth = 6
        binding.cardView2.cardElevation = 30F

        if (Utils.USER == "FACULTY") {
            viewModel.profileDetailsFaculty()
            viewModel.profileFacultyLiveData.observe(viewLifecycleOwner) {
                profileDetailsFaculty = it
                Glide.with(requireActivity()).load(it.profile_pic_firebase).into(binding.ProfilePic)
                name = it.name
                val title: String = when (it.gender) {
                    "M" -> "SIR"
                    "F" -> "MA'AM"
                    else -> "FACULTY"
                }
                binding.CollegeOfUser.text = it.college
                binding.DetailsOfUser.text = it.department
                accountId = it.faculty_account_id
                binding.NameOfUser.text = it.name + " " + title
            }
            viewModel.errorFacultyLiveData.observe(viewLifecycleOwner) {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            }
        } else {
            viewModel.profileDetailsStudent()
            viewModel.profileStudentLiveData.observe(viewLifecycleOwner) {
                profileDetailsStudent = it
                Glide.with(requireActivity()).load(it.profile_pic_firebase).into(binding.ProfilePic)
                name = it.name
                accountId = it.student_account_id
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
            if (it != null) {
                val dialog = Dialog(requireContext())
                dialog.setContentView(PleaseWaitDialogBinding.inflate(layoutInflater).root)
                dialog.setCancelable(false)
                dialog.show()
                if (dialog.window != null)
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(0))

                viewModel.uploadProfilePic(it, accountId)
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
                }
            }
        }

        binding.AddPicBtn.setOnClickListener { chooseImage.launch("image/*") }

        binding.editProfile.setOnClickListener {
            val intent = Intent(context, AskActivity::class.java)
            intent.putExtra("askActivityFragment", Utils.USER)
            if (Utils.USER == "FACULTY") intent.putExtra("profileDetails", profileDetailsFaculty)
            else intent.putExtra("profileDetails", profileDetailsStudent)
            startActivity(intent)
        }

        binding.cardView.setOnClickListener {
            price = 99
            highlightCard(binding.cardView, binding.cardView2)
        }
        binding.cardView2.setOnClickListener {
            price = 199
            highlightCard(binding.cardView2, binding.cardView)
        }

        Checkout.preload(context)
        binding.BuyNowBtn.setOnClickListener {
            try {
                val intent = Intent(activity, PaymentActivity::class.java)
                intent.putExtra("name", name)
                intent.putExtra("price", price.toString())
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
    }

    private fun highlightCard(view1: MaterialCardView, view2: MaterialCardView) {
        view1.strokeColor = Color.parseColor("#804D37")
        view1.strokeWidth = 6
        view1.cardElevation = 30F
        view2.strokeWidth = 0
        view2.cardElevation = 0F
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_profile_to_homePage)
            }
        })
    }
}