package com.example.twowaits.ui.fragment.auth

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.databinding.FragmentOtpVerificationBinding
import com.example.twowaits.repository.authRepository.SendOtpRepository
import com.example.twowaits.repository.authRepository.VerifyOtpRepository
import com.example.twowaits.util.snackBar

class OtpVerification : Fragment(R.layout.fragment_otp_verification) {
    private lateinit var binding: FragmentOtpVerificationBinding
    private lateinit var timerCountDownTimer: CountDownTimer
    private var timerOnStatus: Boolean = true
    private val previousPage by lazy {
        OtpVerificationArgs.fromBundle(requireArguments())
            .previousPage
    }
    private val repository by lazy { SendOtpRepository() }
    private val repository2 by lazy { VerifyOtpRepository() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOtpVerificationBinding.bind(view)
        val userEmail = OtpVerificationArgs.fromBundle(
            requireArguments()
        ).userEmail
        val userPassword = OtpVerificationArgs.fromBundle(
            requireArguments()
        ).userPassword

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

        if (previousPage == "SignUp") repository.sendOtp(userEmail)

        binding.resendOTP.setOnClickListener {
            if (!timerOnStatus) {
                repository.sendOtp(userEmail)
                startTimer()
                timerOnStatus = true
                Toast.makeText(activity, "Resending OTP", Toast.LENGTH_SHORT).show()
            }
        }

        binding.verify.setOnClickListener {
            val otp = binding.EnterOTP.text.toString().trim()
            if (otp.isEmpty()) {
                binding.EnterOTP.error = "Please enter the OTP"
                return@setOnClickListener
            } else if (otp.length != 4) {
                binding.EnterOTP.error = "OTP is incorrect"
                return@setOnClickListener
            }
            binding.verify.isEnabled = false
            binding.verify.text = ""
            binding.progressBar.visibility = View.VISIBLE

            repository2.verifyOtp(VerifyOtpBody(userEmail, otp)).observe(viewLifecycleOwner) {
                if (it.data == "success") {
                    timerCountDownTimer.cancel()
                    if (previousPage == "SignUp") {
                        val action =
                            OtpVerificationDirections.actionOtpVerificationToChooseYourRole(
                                userEmail,
                                userPassword
                            )
                        findNavController().navigate(action)
                    } else {
                        binding.verify.isEnabled = true
                        binding.verify.text = "Verify"
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.EnterOTP.text?.clear()
                        val action =
                            OtpVerificationDirections.actionOtpVerificationToCreatePassword2(
                                userEmail
                            )
                        findNavController().navigate(action)
                    }
                } else {
                    binding.verify.snackBar(it.errorMessage!!)
                    binding.verify.isEnabled = true
                    binding.verify.text = "Verify"
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (previousPage == "SignUp")
                    findNavController().navigate(R.id.action_otpVerification_to_signUp)
                else findNavController().navigate(R.id.action_otpVerification_to_verifyEmail)
            }
        })
    }
}