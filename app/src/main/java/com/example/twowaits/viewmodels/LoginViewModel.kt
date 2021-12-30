package com.example.twowaits.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.twowaits.apiCalls.API
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.repository.LoginRepository

class LoginViewModel(): ViewModel() {
    lateinit var errorLiveData: LiveData<String>
    val api = RetrofitClient.getInstance().create(API::class.java)
    val repository = LoginRepository(api)

    fun login(email: String, password: String){
//        viewModelScope.launch(Dispatchers.IO) {
        repository.login(email, password)
//        }
        errorLiveData = repository.errorMutableLiveData
    }
}