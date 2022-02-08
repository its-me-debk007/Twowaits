package com.example.twowaits.authPages

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.twowaits.Data
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
            hideKeyboard(requireView())
            repository.login(LoginBody(userEmail, userPassword))
            binding.ProgressBar.visibility = View.VISIBLE
            binding.LogInButton.isEnabled = false

            var flag = false
            repository.loginLiveData.observe(viewLifecycleOwner) { loginResponse ->
                repository.getAuthTokens(userEmail, userPassword)
                repository.getTokenLiveData.observe(viewLifecycleOwner) { tokensResponse ->
                    if (loginResponse.type == "faculty") {
                        Data.USER = "FACULTY"
                        lifecycleScope.launch {
                            Data.saveData("log_in_status", "FACULTY")
                            Data.saveData("accessToken", tokensResponse.access)
                            Data.saveData("refreshToken", tokensResponse.refresh)
                            Data.saveData("email", userEmail)
                        }
                    } else {
                        Data.USER = "STUDENT"
                        lifecycleScope.launch {
                            Data.saveData("log_in_status", "STUDENT")
                            Data.saveData("accessToken", tokensResponse.access)
                            Data.saveData("refreshToken", tokensResponse.refresh)
                            Data.saveData("email", userEmail)
                        }
                    }
                    Data.ACCESS_TOKEN = tokensResponse.access
                    Data.REFRESH_TOKEN = tokensResponse.refresh
                    Data.USER_EMAIL = userEmail
                    val intent = Intent(activity, HomeActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                repository.errorData.observe(viewLifecycleOwner) {
                    Toast.makeText(context, "Token error\n$it", Toast.LENGTH_SHORT).show()
                    flag = true
                    binding.Password.text?.clear()
                    binding.LogInButton.isEnabled = true
                    binding.ProgressBar.visibility = View.INVISIBLE
                }
            }
            repository.errorLiveData.observe(viewLifecycleOwner) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                flag = true
                binding.Password.text?.clear()
                binding.LogInButton.isEnabled = true
                binding.ProgressBar.visibility = View.INVISIBLE
            }
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

    private fun showKeyboard(view: View) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard(view: View) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
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