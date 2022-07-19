package com.example.twowaits.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.twowaits.repository.authRepository.LoginRepository
import com.example.twowaits.repository.authRepository.SignUpRepository
import com.example.twowaits.viewModel.AuthViewModel

class AuthViewModelFactory(
    val loginRepository: LoginRepository? = null,
    val signUpRepository: SignUpRepository? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(loginRepository, signUpRepository) as T
    }
}