package com.example.twowaits.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twowaits.model.auth.GettingTokensResponse
import com.example.twowaits.model.auth.LoginBody
import com.example.twowaits.model.auth.LoginResponse
import com.example.twowaits.repository.authRepository.LoginRepository
import com.example.twowaits.repository.authRepository.SignUpRepository
import com.example.twowaits.sealedClass.Response
import kotlinx.coroutines.launch

class AuthViewModel(private val loginRepository: LoginRepository? = null,
                    private val signUpRepository: SignUpRepository? = null) : ViewModel() {
    lateinit var loginLiveData : MutableLiveData<Response<LoginResponse>>
    fun login(loginBody: LoginBody) = viewModelScope.launch {
        loginLiveData = loginRepository!!.login(loginBody)
    }

    lateinit var signUpLiveData : MutableLiveData<String>
    fun signUp(email: String, password: String) = viewModelScope.launch {
        signUpLiveData = signUpRepository!!.signUp(email, password)
    }

    lateinit var tokenLiveData : MutableLiveData<Response<GettingTokensResponse>>
    fun getAuthTokens(email: String, password: String) = viewModelScope.launch {
        tokenLiveData = loginRepository!!.getAuthTokens(email, password)
    }
}