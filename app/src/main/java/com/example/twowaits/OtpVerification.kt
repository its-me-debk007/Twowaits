package com.example.twowaits

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.twowaits.databinding.OtpVerficationBinding

class OtpVerification : Fragment() {
    private var _binding: OtpVerficationBinding? = null
    private val binding get() = _binding!!

    private lateinit var timerCountDownTimer: CountDownTimer
    var timerOnStatus: Boolean = true

override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
    _binding = OtpVerficationBinding.inflate(inflater, container, false)

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

    binding.resendOTP.setOnClickListener{
        if(!timerOnStatus){
            timerOnStatus = true
            startTimer()
            Toast.makeText(activity,"Resending OTP", Toast.LENGTH_SHORT).show()
        }
    }

    binding.verify.setOnClickListener{
        if(binding.EnterOTP.text.toString().isEmpty()) {
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