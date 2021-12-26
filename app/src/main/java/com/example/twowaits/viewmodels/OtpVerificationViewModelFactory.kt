package com.example.twowaits.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.twowaits.repository.BaseRepository

class OtpVerificationViewModelFactory(private val repository: BaseRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OtpVerificationViewModel(repository) as T
    }
}