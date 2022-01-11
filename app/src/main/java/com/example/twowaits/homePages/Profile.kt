package com.example.twowaits.homePages

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.twowaits.databinding.ProfileBinding
import coil.transform.CircleCropTransformation
import com.example.twowaits.CompanionObjects
import com.example.twowaits.AuthActivity
import com.example.twowaits.R
import com.example.twowaits.apiCalls.dashboardApiCalls.QnAResponseItem
import com.example.twowaits.recyclerAdapters.ItemClicked
import com.example.twowaits.viewmodels.ProfileDetailsViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Profile : Fragment(), ItemClicked {
    private var _binding: ProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileBinding.inflate(inflater, container, false)

        val viewModel = ViewModelProvider(this)[ProfileDetailsViewModel::class.java]
        val authToken = CompanionObjects.accessToken

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

        val chooseImage = registerForActivityResult(ActivityResultContracts.GetContent()
        ) {
            binding.ProfilePic.setImageURI(it)
        }

        binding.AddPicBtn.setOnClickListener {
            chooseImage.launch("image/*")
            viewModel.uploadProfilePic()
        }

        binding.BuyNowBtn.setOnClickListener {
        }

        viewModel.getQnA()
//        viewModel.q_n_aMutableLiveData.observe(viewLifecycleOwner, {
//            binding.ProfileQnARecyclerView.adapter = YourQuestionsRecyclerAdapter( 2, this)
//            binding.ProfileQnARecyclerView.layoutManager = LinearLayoutManager(container?.context)
//        })
        viewModel.errorMutableLiveData.observe(viewLifecycleOwner, {
            if (it == "Token has Expired"){
                GlobalScope.launch {
                    val refreshToken = CompanionObjects.readRefreshToken("refreshToken")
                    viewModel.getNewAccessToken(refreshToken!!)
                }
                viewModel.saveRefreshTokenMutableLiveData.observe(viewLifecycleOwner, {
                    GlobalScope.launch {
                        CompanionObjects.saveAccessToken("accessToken", it)
                        CompanionObjects.accessToken = it
                        viewModel.profileDetails("Bearer $authToken") // Calling function again to get profile Details
                        viewModel.getQnA()
                    }
                })
            } else {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })

        binding.YourQuestions.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_yourQuestions)
        }

        binding.MoreYourQuestions.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_yourQuestions)
        }

        binding.Logout.setOnClickListener {
            lifecycleScope.launch {
                CompanionObjects.saveLoginStatus("log_in_status", "false")
            }
            val intent = Intent (context, AuthActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClicked(item: QnAResponseItem) {
        Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
    }
}