package com.example.twowaits.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twowaits.apiCalls.CatFacts
import com.example.twowaits.repository.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: BaseRepository): ViewModel() {
    init {     // In place of init, we just have to use setOnClickListener
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCatFacts()
        }
    }
    val facts: LiveData<CatFacts>
    get() = repository.facts
}