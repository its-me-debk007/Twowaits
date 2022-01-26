package com.example.twowaits.authPages

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.example.twowaits.databinding.LoginBinding
import com.example.twowaits.repository.authRepositories.LoginRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class Login : Fragment() {
    private var _binding: LoginBinding? = null
    private val binding get() = _binding!!

    private var mGoogleSignInClient: GoogleSignInClient? = null

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
                binding.textInputLayout2.helperText = "Please enter a valid email"
                return@setOnClickListener
            }
            if (userPassword.isEmpty()) {
                binding.textInputLayout2.helperText = ""
                binding.textInputLayout.helperText = "Please enter your Password!"
                return@setOnClickListener
            }
            binding.textInputLayout.helperText = ""
            repository.login(LoginBody(userEmail, userPassword))
            Log.d("VVVV", userEmail+'\n'+userPassword)
            binding.ProgressBar.visibility = View.VISIBLE
            binding.LogInButton.isEnabled = false

            var flag = false
            repository.loginLiveData.observe(viewLifecycleOwner, { loginResponse ->
                repository.getAuthTokens(userEmail, userPassword)
                repository.getTokenLiveData.observe(viewLifecycleOwner, { tokensResponse ->
                    if (loginResponse.type == "faculty") {
                        CompanionObjects.USER = "FACULTY"
                        lifecycleScope.launch {
                            CompanionObjects.saveData("log_in_status", "FACULTY")
                            CompanionObjects.saveData("accessToken", tokensResponse.access)
                            CompanionObjects.saveData("refreshToken", tokensResponse.refresh)
                        }
                    } else {
                        CompanionObjects.USER = "STUDENT"
                        lifecycleScope.launch {
                            CompanionObjects.saveData("log_in_status", "STUDENT")
                            CompanionObjects.saveData("accessToken", tokensResponse.access)
                            CompanionObjects.saveData("refreshToken", tokensResponse.refresh)
                        }
                    }
                    CompanionObjects.ACCESS_TOKEN = tokensResponse.access
                    CompanionObjects.REFRESH_TOKEN = tokensResponse.refresh
                    Log.d("VVVV", CompanionObjects.ACCESS_TOKEN.toString())
                    val intent = Intent(activity, HomeActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                })
                repository.errorData.observe(viewLifecycleOwner, {
                    Toast.makeText(context, "Token error\n$it", Toast.LENGTH_SHORT).show()
                    flag = true
                    binding.Password.text?.clear()
                    binding.LogInButton.isEnabled = true
                    binding.ProgressBar.visibility = View.INVISIBLE
                })
            })
            repository.errorLiveData.observe(viewLifecycleOwner, {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                flag = true
                binding.Password.text?.clear()
                binding.LogInButton.isEnabled = true
                binding.ProgressBar.visibility = View.INVISIBLE
            })
            if (flag)
                return@setOnClickListener
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())

        binding.signInButton.setOnClickListener {
            val signInIntent = mGoogleSignInClient!!.signInIntent
            startActivityForResult(signInIntent, 100)
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            val personName: String? = account.displayName
            val personGivenName: String? = account.givenName
            val personFamilyName: String? = account.familyName
            val personEmail: String? = account.email
            val personId: String? = account.id
            val personPhoto: Uri? = account.photoUrl

            Toast.makeText(context, "Hello, $personName ($personEmail)", Toast.LENGTH_SHORT).show()
        } catch (e: ApiException) {
            Log.d("SIGN IN ATTEMPT", "signInResult:failed code=" + e.statusCode)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}