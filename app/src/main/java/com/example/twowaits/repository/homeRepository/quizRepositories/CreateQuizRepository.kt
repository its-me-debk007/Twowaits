package com.example.twowaits.repository.homeRepository.quizRepositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.util.Utils
import com.example.twowaits.network.ServiceBuilder
import com.example.twowaits.network.dashboardApiCalls.quizApiCalls.CreateQuizResponse
import com.example.twowaits.model.CreateQuizBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateQuizRepository {
    private val createQuizMutableLiveData = MutableLiveData<CreateQuizResponse>()
    val createQuizLiveData: LiveData<CreateQuizResponse> = createQuizMutableLiveData
    private val errorMutableLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = errorMutableLiveData

    fun createQuiz(createQuizBody: CreateQuizBody) {
        ServiceBuilder.getInstance().createQuiz(createQuizBody)
            .enqueue(object : Callback<CreateQuizResponse> {
                override fun onResponse(
                    call: Call<CreateQuizResponse>,
                    response: Response<CreateQuizResponse>
                ) {
                    when {
                        response.isSuccessful -> {
                            createQuizMutableLiveData.postValue(response.body())
                        }
                        response.code() == 400 -> {
                            val result = Utils().getNewAccessToken()
//                            if (result == "success") createQuiz(createQuizBody)
//                            else
//                                errorMutableLiveData.postValue("Some error has occurred!\nPlease try again")
                            createQuiz(createQuizBody)
                        }
                        else -> {
                            errorMutableLiveData.postValue(
                                response.code().toString() + response.message()
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<CreateQuizResponse>, t: Throwable) {
                    errorMutableLiveData.postValue(t.message)
                }
            })
    }
}