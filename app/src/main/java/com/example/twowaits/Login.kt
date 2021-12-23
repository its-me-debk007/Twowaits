package com.example.twowaits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.login.view.*

class Login: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.login, container, false)
        v.signUpLink.setOnClickListener{
            Navigation.findNavController(v).navigate(R.id.action_login3_to_signUp)
        }
        v.ForgotPassword.setOnClickListener{
            Navigation.findNavController(v).navigate(R.id.action_login3_to_verifyEmail)
        }


        return v
    }
}