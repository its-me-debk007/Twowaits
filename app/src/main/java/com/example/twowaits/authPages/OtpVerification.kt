package com.example.twowaits.authPages

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.twowaits.CompanionObjects
import com.example.twowaits.HomeActivity
import com.example.twowaits.R
import com.example.twowaits.apiCalls.API
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.databinding.OtpVerificationBinding
import com.example.twowaits.repository.authRepositories.SendOtpRepository
import com.example.twowaits.repository.authRepositories.VerifyOtpRepository

class OtpVerification : Fragment() {
    private var _binding: OtpVerificationBinding? = null
    private val binding get() = _binding!!

    private lateinit var timerCountDownTimer: CountDownTimer
    var timerOnStatus: Boolean = true

override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
    _binding = OtpVerificationBinding.inflate(inflater, container, false)
    val api = RetrofitClient.getInstance().create(API::class.java)
    val repository = SendOtpRepository(api)
    val repository2 = VerifyOtpRepository(api)
//    otpVerificationViewModel = ViewModelProvider(this, OtpVerificationViewModelFactory(repository))[OtpVerificationViewModel::class.java]

//    otpVerificationViewModel.sendOtp(SignUp.EMAIL)

    fun startTimer() {
        timerCountDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val timeLeft = millisUntilFinished / 1000
                binding.resendOTP.text = "Resend OTP after $timeLeft sec(s)"
            }
            override fun onFinish() {
                binding.resendOTP.text = "Resend OTP"
                timerOnStatus = false
            }

        }.start()
    }
    startTimer()

    if (CompanionObjects.PREVIOUS_PAGE == "SignUp")
        repository.sendOtp(CompanionObjects.EMAIL)

    binding.resendOTP.setOnClickListener{
            if (!timerOnStatus){
                repository.sendOtp(CompanionObjects.EMAIL)
                startTimer()
                timerOnStatus = true
                Toast.makeText(activity,"Resending OTP", Toast.LENGTH_SHORT).show()
            }
    }

    binding.verify.setOnClickListener{
        val api = RetrofitClient.getInstance().create(API::class.java)
        val repository = SendOtpRepository(api)
        val repository2 = VerifyOtpRepository(api)

        val otp = binding.EnterOTP.text.toString().trim()
        if(otp.isEmpty()) {
            binding.EnterOTP.error = "Please enter the OTP"
            return@setOnClickListener
        }
        if(otp.length != 4){
            binding.EnterOTP.error = "OTP is incorrect"
            return@setOnClickListener
        }

        repository2.verifyOtp(CompanionObjects.EMAIL, otp)
        binding.verify.isEnabled = false
        binding.ProgressBar.visibility = View.VISIBLE

        var flag = false
        repository2.errorMutableLiveData.observe(viewLifecycleOwner, {
            if (it == "success"){
                if (CompanionObjects.PREVIOUS_PAGE == "SignUp") {
                    val intent = Intent(activity, HomeActivity::class.java)
                    startActivity(intent)
                    timerCountDownTimer.cancel()
                }
                else if (CompanionObjects.PREVIOUS_PAGE == "VerifyEmail"){
                    Navigation.findNavController(binding.root).navigate(R.id.action_otpVerification_to_createPassword2)
                    timerCountDownTimer.cancel()
                }
            }
            else {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                binding.verify.isEnabled = true
                binding.ProgressBar.visibility = View.INVISIBLE
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