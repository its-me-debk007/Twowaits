package com.example.twowaits.AuthPages

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.twowaits.AuthPages.SignUp.Companion.EMAIL
import com.example.twowaits.AuthPages.SignUp.Companion.PREVIOUS_PAGE
import com.example.twowaits.HomeActivity
import com.example.twowaits.R
import com.example.twowaits.apiCalls.API
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.databinding.OtpVerificationBinding
import com.example.twowaits.repository.SendOtpRepository
import com.example.twowaits.repository.VerifyOtpRepository
import com.example.twowaits.viewmodels.OtpVerificationViewModel
import com.example.twowaits.viewmodels.OtpVerificationViewModelFactory

class OtpVerification : Fragment() {
    private var _binding: OtpVerificationBinding? = null
    private val binding get() = _binding!!
    private lateinit var otpVerificationViewModel: OtpVerificationViewModel

override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
    _binding = OtpVerificationBinding.inflate(inflater, container, false)
    val api = RetrofitClient.getInstance().create(API::class.java)
    val repository = SendOtpRepository(api)
    val repository2 = VerifyOtpRepository(api)
    otpVerificationViewModel = ViewModelProvider(this, OtpVerificationViewModelFactory(repository))[OtpVerificationViewModel::class.java]

//    otpVerificationViewModel.sendOtp(SignUp.EMAIL)
    otpVerificationViewModel.timerLiveData.observe(viewLifecycleOwner,{
        binding.resendOTP.text = "Resend OTP after $it sec(s)"
    })
    otpVerificationViewModel.timerOnStatusLiveData.observe(viewLifecycleOwner,{
            binding.resendOTP.text = "Resend OTP"
    })

    if (PREVIOUS_PAGE == "SignUp")
        otpVerificationViewModel.sendOtp(EMAIL)

    binding.resendOTP.setOnClickListener{
            if (!otpVerificationViewModel.timerOnStatus){
                otpVerificationViewModel.sendOtp(EMAIL)
                otpVerificationViewModel.startTimer()
                otpVerificationViewModel.timerOnStatus = true
                Toast.makeText(activity,"Resending OTP", Toast.LENGTH_SHORT).show()
            }
    }

    binding.verify.setOnClickListener{
        val otp = binding.EnterOTP.text.toString().trim()
        if(otp.isEmpty()) {
            binding.EnterOTP.error = "Please enter the OTP"
            return@setOnClickListener
        }
        if(otp.length != 4){
            binding.EnterOTP.error = "OTP is incorrect"
            return@setOnClickListener
        }

        otpVerificationViewModel.verifyOtp(EMAIL, otp)
        binding.verify.isEnabled = false

        var flag = false
        otpVerificationViewModel.verifyOtpLiveData.observe(viewLifecycleOwner, {
            if (it == "success"){
                if (PREVIOUS_PAGE == "SignUp") {
                    val intent = Intent(activity, HomeActivity::class.java)
                    startActivity(intent)
                }
                else if (PREVIOUS_PAGE == "VerifyEmail"){
                    Navigation.findNavController(binding.root).navigate(R.id.action_otpVerification_to_createPassword2)
                }
            }
            else {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                binding.verify.isEnabled = true
                flag = true
            }
        })
        if (flag)
            return@setOnClickListener
    }

//    Before Moving to next fragment, write this:
//      timerCountDownTimer.cancel()

        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}