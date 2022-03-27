package com.example.twowaits.authPages

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.twowaits.Data
import com.example.twowaits.R
import com.example.twowaits.databinding.ChooseYourRoleBinding
import com.example.twowaits.repository.authRepositories.LoginRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class ChooseYourRole : Fragment(R.layout.choose_your_role) {
    private lateinit var binding: ChooseYourRoleBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ChooseYourRoleBinding.bind(view)
        val userEmail = ChooseYourRoleArgs.fromBundle(requireArguments()).userEmail
        val userPassword = ChooseYourRoleArgs.fromBundle(requireArguments()).userPassword
        binding.apply {
            student.setOnCheckedChangeListener { _, _ ->
                val repository = LoginRepository()
                repository.getAuthTokens(userEmail, userPassword)
                Data.EMAIL = userEmail
                ProgressBar.visibility = View.VISIBLE
                repository.getTokenLiveData.observe(viewLifecycleOwner) { tokensResponse ->
                    lifecycleScope.launch {
                        Data.saveData("log_in_status", "STUDENT")
                        Data.saveData("accessToken", tokensResponse.access)
                        Data.saveData("refreshToken", tokensResponse.refresh)
                    }
                    Data.ACCESS_TOKEN = tokensResponse.access
                    Data.REFRESH_TOKEN = tokensResponse.refresh
                    val action =
                        ChooseYourRoleDirections.actionChooseYourRoleToEnterDetailsStudent(userEmail)
                    findNavController().navigate(action)
                }
                repository.errorData.observe(viewLifecycleOwner) {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    Log.e("ffff", it)
                }
            }
            teacher.setOnCheckedChangeListener { _, _ ->
                val repository = LoginRepository()
                repository.getAuthTokens(userEmail, userPassword)
                ProgressBar.visibility = View.VISIBLE
                repository.getTokenLiveData.observe(viewLifecycleOwner) { tokensResponse ->
                    lifecycleScope.launch {
                        Data.saveData("log_in_status", "FACULTY")
                        Data.saveData("accessToken", tokensResponse.access)
                        Data.saveData("refreshToken", tokensResponse.refresh)
                    }
                    Data.ACCESS_TOKEN = tokensResponse.access
                    Data.REFRESH_TOKEN = tokensResponse.refresh
                    teacher.isChecked = false
                    student.isChecked = false
                    ProgressBar.visibility = View.INVISIBLE
                    val action =
                        ChooseYourRoleDirections.actionChooseYourRoleToEnterDetailsFaculty(userEmail)
                    findNavController().navigate(action)
                }
                repository.errorData.observe(viewLifecycleOwner) {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    Log.e("ffff", it)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_chooseYourRole_to_otpVerification)
            }
        })
    }
}