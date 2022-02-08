package com.example.twowaits.authPages

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.twowaits.Data
import com.example.twowaits.R
import com.example.twowaits.databinding.OtpVerificationBinding
import com.example.twowaits.repository.authRepositories.SendOtpRepository
import com.example.twowaits.repository.authRepositories.VerifyOtpRepository
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class OtpVerification : Fragment() {
    private var _binding: OtpVerificationBinding? = null
    private val binding get() = _binding!!

    private lateinit var timerCountDownTimer: CountDownTimer
    private var timerOnStatus: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OtpVerificationBinding.inflate(inflater, container, false)

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

        if (Data.PREVIOUS_PAGE == "SignUp") {
            val repository = SendOtpRepository()
            repository.sendOtp(Data.EMAIL)
        }

        binding.resendOTP.setOnClickListener {
            if (!timerOnStatus) {
                val repository = SendOtpRepository()
                repository.sendOtp(Data.EMAIL)
                startTimer()
                timerOnStatus = true
                Toast.makeText(activity, "Resending OTP", Toast.LENGTH_SHORT).show()
            }
        }

        binding.verify.setOnClickListener {
            val repository2 = VerifyOtpRepository()
            val otp = binding.EnterOTP.text.toString().trim()
            if (otp.isEmpty()) {
                binding.EnterOTP.error = "Please enter the OTP"
                return@setOnClickListener
            }
            else if (otp.length != 4) {
                binding.EnterOTP.error = "OTP is incorrect"
                return@setOnClickListener
            }
            repository2.verifyOtp(VerifyOtpBody(Data.EMAIL, otp))
            binding.verify.isEnabled = false
            binding.ProgressBar.visibility = View.VISIBLE

            var flag = false
            repository2.errorMutableLiveData.observe(viewLifecycleOwner) {
                if (it == "success") {
                    if (Data.PREVIOUS_PAGE == "SignUp") {
                        timerCountDownTimer.cancel()
                        findNavController().navigate(R.id.action_otpVerification_to_chooseYourRole)
                    } else if (Data.PREVIOUS_PAGE == "VerifyEmail") {
                        timerCountDownTimer.cancel()
                        binding.verify.isEnabled = true
                        binding.ProgressBar.visibility = View.INVISIBLE
                        binding.EnterOTP.text?.clear()
                        findNavController().navigate(R.id.action_otpVerification_to_createPassword2)
                    }
                } else {
                    Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                    binding.verify.isEnabled = true
                    binding.ProgressBar.visibility = View.INVISIBLE
                }
            }
        }
//    Before Moving to next fragment:
//      timerCountDownTimer.cancel()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (Data.PREVIOUS_PAGE == "SignUp")
                    findNavController().navigate(R.id.action_otpVerification_to_signUp)
                else if (Data.PREVIOUS_PAGE == "VerifyEmail")
                    findNavController().navigate(R.id.action_otpVerification_to_verifyEmail)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}