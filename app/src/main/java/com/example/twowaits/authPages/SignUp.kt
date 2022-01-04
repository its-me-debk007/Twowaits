package com.example.twowaits.authPages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.twowaits.CompanionObjects
import com.example.twowaits.R
import com.example.twowaits.apiCalls.API
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.databinding.SignUpBinding
import com.example.twowaits.repository.authRepositories.BaseRepository

class SignUp : Fragment() {
    private var _binding: SignUpBinding? = null
    private val binding get() = _binding!!
//    private lateinit var signUpViewModel: SignUpViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignUpBinding.inflate(inflater, container, false)

//        signUpViewModel = ViewModelProvider(this, SignUpViewModelFactory(repository))[SignUpViewModel::class.java]

        binding.loginLink.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.action_signUp_to_login3)
        }
        binding.VerifyEmailButton.setOnClickListener {
            val api = RetrofitClient.getInstance().create(API::class.java)
            val repository = BaseRepository(api)

            val userEmail = binding.EnterEmail.text.toString().trim()
            if(!Login().isValidEmail(userEmail)){
                binding.EnterEmail.error="Please enter a valid email!"
                return@setOnClickListener
            }
            var flagLower = false
            var flagUpper = false
            var flagNumber = false
            var flagSpecialChar = false
            for (i in binding.password.text.toString().indices) {
                val ch = binding.password.text.toString()[i]
                if (ch in 'a'..'z') {
                    flagLower = true
                }
                if (ch in 'A'..'Z') {
                    flagUpper = true
                }
                if (ch in '0'..'9') {
                    flagNumber = true
                }
//                if(ch == '#' || ch == '%' || ch == '@' || ch == '$' || ch == '^' || ch == '&' || ch == '+' || ch == '='){
//                    flagSpecialChar = true
//                }
                val asciiCode = ch.code
                if((asciiCode in 32..47)||(asciiCode in 58..64)||(asciiCode in 91..96)||(asciiCode in 123..126)){
                    flagSpecialChar = true
                }
            }
            if(binding.password.text.toString().length < 8){
                binding.textInputLayout.helperText = "Password must contain at least 8 characters"
                return@setOnClickListener
            }
            if(!flagLower){
                binding.textInputLayout.helperText = "Password must contain at least one lower case letter"
                return@setOnClickListener
            }
            if(!flagUpper){
                binding.textInputLayout.helperText = "Password must contain at least one upper case letter"
                return@setOnClickListener
            }
            if(!flagNumber){
                binding.textInputLayout.helperText = "Password must contain at least one numeric digit"
                return@setOnClickListener
            }
            if(!flagSpecialChar){
                binding.textInputLayout.helperText = "Password must contain at least one special character"
                return@setOnClickListener
            }
            if(binding.password.text.toString().contains("123")){
                binding.textInputLayout.helperText = "Password should not contain 123"
                return@setOnClickListener
            }
            binding.textInputLayout.helperText = ""
            if (binding.confirmPassword.text.toString() != binding.password.text.toString()){
                binding.textInputLayout3.helperText = "The two passwords do not match"
                return@setOnClickListener
            }
            binding.textInputLayout3.helperText = ""
            repository.signUp(userEmail, binding.confirmPassword.text.toString())
            binding.VerifyEmailButton.isEnabled = false
            binding.ProgressBar.visibility = View.VISIBLE

            var flag = false
            repository.errorMutableLiveData.observe(viewLifecycleOwner, {
                if (it == "success"){
                    CompanionObjects.EMAIL = userEmail
                    CompanionObjects.PREVIOUS_PAGE = "SignUp"
                    findNavController().navigate(R.id.action_signUp_to_otpVerification)
                }
                else {
                    Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                    binding.password.text?.clear()
                    binding.confirmPassword.text?.clear()
                    binding.VerifyEmailButton.isEnabled = true
                    binding.ProgressBar.visibility = View.INVISIBLE
                    flag = true
                }
            })
            if (flag)
                return@setOnClickListener
        }
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}