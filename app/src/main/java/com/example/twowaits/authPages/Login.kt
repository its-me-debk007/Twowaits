package com.example.twowaits.authPages

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.twowaits.CompanionObjects
import com.example.twowaits.HomeActivity
import com.example.twowaits.R
import com.example.twowaits.apiCalls.API
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.databinding.LoginBinding
import com.example.twowaits.repository.authRepositories.LoginRepository
import kotlinx.coroutines.launch


class Login : Fragment() {
    private var _binding: LoginBinding? = null
    private val binding get() = _binding!!

    fun isValidEmail(str: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginBinding.inflate(inflater, container, false)

        binding.signUpLink.setOnClickListener {
            findNavController().navigate(R.id.action_login3_to_signUp)
        }
        binding.ForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_login3_to_verifyEmail)
        }
        binding.LogInButton.setOnClickListener {
            val repository = LoginRepository()

            val userEmail = binding.EmailAddress.text.toString().trim()
            val userPassword = binding.Password.text.toString()

            if (!isValidEmail(userEmail)) {
                binding.EmailAddress.error = "Please enter a valid email"
                return@setOnClickListener
            }
            if (userPassword.isEmpty()) {
                binding.textInputLayout.helperText = "Please enter your Password!"
                return@setOnClickListener
            }
            binding.textInputLayout.helperText = ""

            repository.login(userEmail, userPassword)
            binding.ProgressBar.visibility = View.VISIBLE
            binding.LogInButton.isEnabled = false

            var flag = false
            repository.errorMutableLiveData.observe(viewLifecycleOwner, {
                if (it == "success") {
                    repository.getAuthTokens(userEmail, userPassword)
                    lifecycleScope.launch {
                        CompanionObjects.saveLoginStatus("log_in_status", "true")
                    }
                    val intent = Intent(activity, HomeActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                } else {
                    Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                    flag = true
                    binding.Password.text?.clear()
                    binding.LogInButton.isEnabled = true
                    binding.ProgressBar.visibility = View.INVISIBLE
                }
            })
            if (flag)
                return@setOnClickListener
        }
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail()
//            .build()
//        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        val account = GoogleSignIn.getLastSignedInAccount(this)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_login_to_firstAuthPage)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}