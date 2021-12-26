package com.example.twowaits.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twowaits.apiCalls.CatFacts
import com.example.twowaits.repository.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: BaseRepository): ViewModel() {

    fun gettingFacts(){
        viewModelScope.launch(Dispatchers.IO) {
                repository.getCatFacts()
        }
    }
    val facts: LiveData<CatFacts> = repository.facts
}