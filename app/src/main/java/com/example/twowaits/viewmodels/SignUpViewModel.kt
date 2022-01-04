package com.example.twowaits.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.twowaits.repository.authRepositories.BaseRepository

class SignUpViewModel(private val repository: BaseRepository): ViewModel() {
    lateinit var errorLiveData: LiveData<String>

    fun signUp(email: String, password: String){
//        viewModelScope.launch(Dispatchers.IO) {
            repository.signUp(email, password)
//        }
        errorLiveData = repository.errorMutableLiveData
    }
}