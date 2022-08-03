package com.example.twowaits.ui.fragment.profile

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.example.twowaits.ui.activity.PaymentActivity
import com.example.twowaits.R
import com.example.twowaits.databinding.FragmentProfileBinding
import com.example.twowaits.network.dashboardApiCalls.FacultyProfileDetailsResponse
import com.example.twowaits.network.dashboardApiCalls.StudentProfileDetailsResponse
import com.example.twowaits.sealedClass.Response
import com.example.twowaits.ui.activity.home.AskActivity
import com.example.twowaits.ui.fragment.home.Wishlist
import com.example.twowaits.ui.fragment.home.YourQuestions
import com.example.twowaits.util.Datastore
import com.example.twowaits.viewModel.ProfileViewModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.razorpay.Checkout
import kotlinx.coroutines.launch

class Profile : Fragment(R.layout.fragment_profile) {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel by lazy { ViewModelProvider(this)[ProfileViewModel::class.java] }
    private lateinit var userName: String
    private var accountId = 0
    private lateinit var profileDetailsStudent: StudentProfileDetailsResponse
    private lateinit var profileDetailsFaculty: FacultyProfileDetailsResponse
    private val dataStore by lazy { Datastore(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        var price = 199

        binding.swipeToRefresh.setColorSchemeColors(Color.parseColor("#804D37"))
        binding.swipeToRefresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_profile_self)
            }, 440)
        }

        viewModel.getProfile()
        viewModel.profileLiveData.observe(viewLifecycleOwner) {
            if (it is Response.Success) {
                it.data!!.apply {
                    Glide.with(requireActivity())
                        .load(profile_pic_firebase)
                        .placeholder(R.drawable.ic_placeholder)
                        .into(binding.ProfilePic)

                    userName = name
                    val nameWithTitle = faculty_account_id?.let { _ ->
                        "$name " + when (gender) {
                            "M" -> "SIR"
                            "F" -> "MA'AM"
                            else -> "FACULTY"
                        }
                    } ?: name
                    binding.NameOfUser.text = nameWithTitle
                    binding.CollegeOfUser.text = college
                    binding.DetailsOfUser.text =
                        department ?: "$year | $course | $branch"
                    accountId = faculty_account_id ?: student_account_id!!

                    faculty_account_id?.let { _ ->
                        profileDetailsFaculty = FacultyProfileDetailsResponse(
                            department!!,
                            faculty_account_id,
                            name,
                            profile_pic,
                            dob,
                            college,
                            gender,
                            profile_pic_firebase
                        )
                    } ?: run {
                        profileDetailsStudent = StudentProfileDetailsResponse(
                            branch!!,
                            college,
                            course!!,
                            name,
                            profile_pic,
                            student_account_id!!,
                            year!!,
                            null,
                            gender,
                            dob,
                            profile_pic_firebase!!
                        )
                    }
                }
            }
        }

        val chooseImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let {
                binding.profileProgressBar.show()
                binding.ProfilePic.alpha = 0.2f
                viewModel.uploadPicFirebase(it, accountId)
                viewModel.firebaseLiveData.observe(viewLifecycleOwner) { message ->
                    if (message.substring(0, 8) == "Uploaded") {
                        val uri = message.substring(8)
                        lifecycleScope.launch {
                            if (dataStore.readLoginState() == "FACULTY") profileDetailsFaculty.profile_pic_firebase =
                                uri
                            else profileDetailsStudent.profile_pic_firebase = uri
                        }
                        viewModel.updateProfilePic(userName, uri)
                        viewModel.updateProfilePicLiveData.observe(viewLifecycleOwner) { response ->
                            if (response != "success")
                                Toast.makeText(context, response, Toast.LENGTH_SHORT).show()
                            binding.ProfilePic.setImageURI(it)
                        }
                    } else Toast.makeText(context, "$message\nPlease try again", Toast.LENGTH_SHORT)
                        .show()

                    binding.profileProgressBar.hide()
                    binding.ProfilePic.alpha = 1f
                }
            }
        }

        binding.AddPicBtn.setOnClickListener { chooseImage.launch("image/*") }

        binding.editProfile.setOnClickListener {
            Intent(context, AskActivity::class.java).apply {
                lifecycleScope.launch {
                    putExtra("askActivityFragment", dataStore.readLoginState())
                    if (dataStore.readLoginState() == "FACULTY") putExtra(
                        "profileDetails",
                        profileDetailsFaculty
                    )
                    else putExtra("profileDetails", profileDetailsStudent)
                    startActivity(this@apply)
                }
            }
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
                intent.putExtra("name", userName)
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
        view1.strokeWidth = 5
        view1.cardElevation = 12F
        view2.strokeWidth = 0
        view2.cardElevation = 0F
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
        returnTransition = MaterialFadeThrough()

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_profile_to_homePage)
            }
        })
    }
}

class ProfileViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> YourQuestions()
            else -> Wishlist()
        }
    }
}