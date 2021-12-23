package com.example.twowaits

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

class OtpVerfication : Fragment() {
override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.otp_verfication, container, false)
//        Navigation.findNavController(v).navigate(R.id.action_testFragment_to_testFragment2)
        return v
    }
}