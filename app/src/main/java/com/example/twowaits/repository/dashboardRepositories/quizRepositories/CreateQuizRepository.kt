package com.example.twowaits.repository.dashboardRepositories.quizRepositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twowaits.Data
import com.example.twowaits.apiCalls.RetrofitClient
import com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls.CreateQuizResponse
import com.example.twowaits.homePages.quiz.CreateQuizBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateQuizRepository {
    private val createQuizMutableLiveData = MutableLiveData<CreateQuizResponse>()
    val createQuizLiveData: LiveData<CreateQuizResponse> = createQuizMutableLiveData
    private val errorMutableLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = errorMutableLiveData

    fun createQuiz(createQuizBody: CreateQuizBody) {
        RetrofitClient.getInstance().createQuiz(createQuizBody)
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
                            val result = Data().getNewAccessToken()
                            if (result == "success")
                                createQuiz(createQuizBody)
                            else
                                errorMutableLiveData.postValue("Some error has occurred!\nPlease try again")
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