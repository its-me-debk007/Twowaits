package com.example.twowaits.ui.fragment.navDrawer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.twowaits.R
import com.example.twowaits.databinding.PrivacyPolicyBinding

class PrivacyPolicy : Fragment(R.layout.privacy_policy) {
    private lateinit var binding: PrivacyPolicyBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PrivacyPolicyBinding.bind(view)
    }
}