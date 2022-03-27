package com.example.twowaits.homePages.navdrawerPages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.twowaits.R
import com.example.twowaits.databinding.PrivacyPolicyBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class PrivacyPolicy : Fragment(R.layout.privacy_policy) {
    private lateinit var binding: PrivacyPolicyBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PrivacyPolicyBinding.bind(view)
    }
}