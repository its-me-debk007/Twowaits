package com.example.twowaits.ui.fragments.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.utils.Utils
import com.example.twowaits.databinding.FragmentLoginBinding
import com.example.twowaits.models.auth.LoginBody
import com.example.twowaits.sealedClasses.Response
import com.example.twowaits.repositories.authRepositories.LoginRepository
import com.example.twowaits.ui.activities.home.HomeActivity
import com.example.twowaits.utils.Datastore
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val repository by lazy { LoginRepository() }
    private val datastore by lazy { Datastore(requireContext()) }

    fun isValidEmail(str: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        binding.apply {

            signUp.setOnClickListener {
                findNavController().navigate(R.id.action_login3_to_signUp)
            }
            forgotPassword.setOnClickListener {
                findNavController().navigate(R.id.action_login3_to_verifyEmail)
            }
            logIn.setOnClickListener {
                textInputLayout2.helperText = ""
                textInputLayout.helperText = ""

                val userEmail = EmailAddress.text.toString().trim()
                val userPassword = password.text.toString()
                if (!isValidEmail(userEmail)) {
                    textInputLayout2.helperText = "Please enter a valid email"
                    return@setOnClickListener
                }
                if (userPassword.isEmpty()) {
                    textInputLayout.helperText = "Please enter your Password!"
                    return@setOnClickListener
                }

                Utils().hideKeyboard(requireView(), activity)
                binding.ProgressBar.visibility = View.VISIBLE
                logIn.isEnabled = false

                var flag = false
                repository.login(LoginBody(userEmail, userPassword))
                    .observe(viewLifecycleOwner) { loginResponse ->

                        if (loginResponse is Response.Success) {
                            repository.getAuthTokens(userEmail, userPassword)
                                .observe(viewLifecycleOwner) { tokensResponse ->
                                    if (tokensResponse is Response.Success) {

                                        if (loginResponse.data?.type == "faculty") {
                                            Utils.USER = "FACULTY"
                                            lifecycleScope.launch {
                                                datastore.saveLoginData("FACULTY")
                                            }
                                        } else {
                                            Utils.USER = "STUDENT"
                                            lifecycleScope.launch {
                                                datastore.saveLoginData("STUDENT")
                                            }
                                        }

                                        lifecycleScope.launch {
                                            datastore.saveTokens("accessToken",
                                                tokensResponse.data!!.access)
                                            datastore.saveTokens("refreshToken",
                                                tokensResponse.data.refresh)
                                            datastore.saveUserDetails("email", userEmail)
                                        }
                                        Utils.ACCESS_TOKEN = tokensResponse.data!!.access
                                        Utils.REFRESH_TOKEN = tokensResponse.data!!.refresh
//                                        Utils.EMAIL = userEmail

                                        startActivity(Intent(activity, HomeActivity::class.java))
                                        activity?.finish()
                                    } else if (tokensResponse is Response.Error) {
                                        val snackBar = Snackbar.make(
                                            logIn,
                                            tokensResponse.errorMessage!!,
                                            Snackbar.LENGTH_SHORT
                                        )
                                        snackBar.apply {
                                            setAction("OK") {
                                                dismiss()
                                            }
                                            animationMode = Snackbar.ANIMATION_MODE_SLIDE
                                            show()
                                        }
                                        password.text?.clear()
                                        logIn.isEnabled = true
                                        ProgressBar.visibility = View.INVISIBLE
                                        flag = true
                                    }
                                }
                        } else if (loginResponse is Response.Error) {
                            val snackBar = Snackbar.make(
                                logIn,
                                loginResponse.errorMessage!!,
                                Snackbar.LENGTH_SHORT
                            )
                            snackBar.apply {
                                setAction("OK") {
                                    dismiss()
                                }
                                animationMode = Snackbar.ANIMATION_MODE_SLIDE
                                show()
                            }
                            password.text?.clear()
                            logIn.isEnabled = true
                            ProgressBar.visibility = View.INVISIBLE
                            flag = true
                        }
                    }

                if (flag) return@setOnClickListener
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_login_to_firstAuthPage)
            }
        })
    }
}