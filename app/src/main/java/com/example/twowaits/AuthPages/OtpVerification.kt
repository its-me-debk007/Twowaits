package com.example.twowaits.AuthPages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.twowaits.apiCalls.API
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.databinding.OtpVerficationBinding
import com.example.twowaits.repository.BaseRepository
import com.example.twowaits.viewmodels.OtpVerificationViewModel
import com.example.twowaits.viewmodels.OtpVerificationViewModelFactory

class OtpVerification : Fragment() {
    private var _binding: OtpVerficationBinding? = null
    private val binding get() = _binding!!
    private lateinit var otpVerificationViewModel: OtpVerificationViewModel

override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
    _binding = OtpVerficationBinding.inflate(inflater, container, false)
    val api = RetrofitClient.getInstance().create(API::class.java)
    val repository = BaseRepository(api)
    otpVerificationViewModel = ViewModelProvider(this, OtpVerificationViewModelFactory(repository))[OtpVerificationViewModel::class.java]

    otpVerificationViewModel.timerLiveData.observe(viewLifecycleOwner,{
        binding.resendOTP.text = "Resend OTP after $it sec(s)"
    })
    otpVerificationViewModel.timerOnStatusLiveData.observe(viewLifecycleOwner,{
            binding.resendOTP.text = "Resend OTP"
    })

    binding.resendOTP.setOnClickListener{
            if (!otpVerificationViewModel.timerOnStatus){
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