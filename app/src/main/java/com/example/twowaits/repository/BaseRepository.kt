package com.example.twowaits.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.apiCalls.API
import com.example.twowaits.apiCalls.CatFacts

class BaseRepository(private val api: API){
    private val factsLiveData = MutableLiveData<CatFacts>()

    val facts: LiveData<CatFacts>
    get() = factsLiveData

    suspend fun getCatFacts(){
        val result = api.getCatFacts()
        if(result.body() != null){
            factsLiveData.postValue(result.body())
        }
    }
}