package com.example.twowaits

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.login.view.*
import kotlinx.android.synthetic.main.sign_up.*
import kotlinx.android.synthetic.main.sign_up.view.*

class SignUp : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.sign_up, container, false)
        v.loginLink.setOnClickListener{
            Navigation.findNavController(v).navigate(R.id.action_signUp_to_login3)
        }
        v.VerifyEmailButton.setOnClickListener {
            val userEmail = EnterEmail.text.toString().trim()
            if(!Login().isValidEmail(userEmail)){
                EnterEmail.error="Please enter a valid email"
                return@setOnClickListener
            }
        }

        return v
    }
}