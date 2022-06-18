package com.example.twowaits.ui.fragments.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.utils.Utils
import com.example.twowaits.databinding.ChooseYourRoleBinding
import com.example.twowaits.sealedClasses.Response
import com.example.twowaits.repositories.authRepositories.LoginRepository
import com.example.twowaits.utils.Datastore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class ChooseYourRole : Fragment(R.layout.choose_your_role) {
    private lateinit var binding: ChooseYourRoleBinding
    private val repository by lazy { LoginRepository() }
    private val dataStore by lazy { Datastore(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ChooseYourRoleBinding.bind(view)
        val userEmail = ChooseYourRoleArgs.fromBundle(
            requireArguments()
        ).userEmail
        val userPassword = ChooseYourRoleArgs.fromBundle(
            requireArguments()
        ).userPassword
//        Utils.EMAIL = userEmail

        binding.apply {
            student.setOnCheckedChangeListener { _, _ ->


                ProgressBar.visibility = View.VISIBLE
                repository.getAuthTokens(userEmail, userPassword).observe(viewLifecycleOwner) {
                    if (it is Response.Success) {
                        lifecycleScope.launch {
                            dataStore.saveLoginData("STUDENT")
                            dataStore.saveTokens("accessToken", it.data!!.access)
                            dataStore.saveTokens("refreshToken", it.data.refresh)
                        }
                        Utils.ACCESS_TOKEN = it.data!!.access
                        Utils.REFRESH_TOKEN = it.data.refresh
                        teacher.isChecked = false
                        student.isChecked = false
                        ProgressBar.visibility = View.INVISIBLE
                        val action =
                            ChooseYourRoleDirections.actionChooseYourRoleToEnterDetailsStudent(
                                userEmail
                            )
                        findNavController().navigate(action)
                    } else if (it is Response.Error) {
                        Toast.makeText(context, it.errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            teacher.setOnCheckedChangeListener { _, _ ->
                repository.getAuthTokens(userEmail, userPassword)
                ProgressBar.visibility = View.VISIBLE
                repository.getAuthTokens(userEmail, userPassword).observe(viewLifecycleOwner) {
                    if (it is Response.Success) {
                        lifecycleScope.launch {
                            dataStore.saveLoginData("FACULTY")
                            dataStore.saveTokens("accessToken", it.data!!.access)
                            dataStore.saveTokens("refreshToken", it.data.refresh)
                        }
                        Utils.ACCESS_TOKEN = it.data!!.access
                        Utils.REFRESH_TOKEN = it.data.refresh
                        teacher.isChecked = false
                        student.isChecked = false
                        ProgressBar.visibility = View.INVISIBLE
                        val action =
                            ChooseYourRoleDirections.actionChooseYourRoleToEnterDetailsFaculty(
                                userEmail
                            )
                        findNavController().navigate(action)
                    } else if (it is Response.Error) {
                        Toast.makeText(context, it.errorMessage, Toast.LENGTH_SHORT).show()
                    }
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