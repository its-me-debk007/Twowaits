package com.example.twowaits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.login.view.*

class Login: Fragment() {
    fun isValidEmail(str: String): Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }

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
        v.LogInButton.setOnClickListener {
            val userEmail = EmailAddress.text.toString().trim()
            if(!isValidEmail(userEmail)){
                EmailAddress.error="Please enter a valid email"
                return@setOnClickListener
            }
        }


        return v
    }
}