package com.example.grocerystoretest.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grocerystoretest.model.request.auth.LoginRequest
import com.example.grocerystoretest.model.response.BaseResponse
import com.example.grocerystoretest.model.response.auth.LoginResponse
import com.example.grocerystoretest.network.RetrofitClient
import com.example.grocerystoretest.utils.ApplicationPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(context: Context) : ViewModel() {

    private val apiService = RetrofitClient.getInstance(context)
    private val applicationPreference = ApplicationPreference.getInstance(context)

    val isSuccessfullyLoggedIn = MutableLiveData<Boolean>()

    fun login(phoneNumber: String, password: String) {
        val loginRequest = LoginRequest(phoneNumber, password)
        apiService
            .login(loginRequest)
            .enqueue(object : Callback<BaseResponse<LoginResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<LoginResponse>>,
                    response: Response<BaseResponse<LoginResponse>>
                ) {
                    if (response.isSuccessful) {
                        isSuccessfullyLoggedIn.value = true
                        val loginResponse = response.body()?.data
                        if (loginResponse != null) {
                            applicationPreference?.saveToken(
                                loginResponse.refreshToken,
                                loginResponse.accessToken
                            )
                        }
                    } else {
                        isSuccessfullyLoggedIn.value = false
                    }
                }

                override fun onFailure(call: Call<BaseResponse<LoginResponse>>, t: Throwable) {
                    isSuccessfullyLoggedIn.value = false
                }

            })
    }

}