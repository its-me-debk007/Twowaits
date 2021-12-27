package com.example.twowaits.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.twowaits.repository.BaseRepository
import com.example.twowaits.repository.SendOtpRepository

class OtpVerificationViewModelFactory(private val repository: SendOtpRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OtpVerificationViewModel() as T
    }
}