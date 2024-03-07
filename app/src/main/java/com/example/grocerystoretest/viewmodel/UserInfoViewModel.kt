package com.example.grocerystoretest.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grocerystoretest.model.User
import com.example.grocerystoretest.model.response.BaseResponse
import com.example.grocerystoretest.model.response.auth.UserInfoResponse
import com.example.grocerystoretest.network.RetrofitClient
import com.example.grocerystoretest.utils.ApplicationPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInfoViewModel(context: Context) : ViewModel() {

    private val apiService = RetrofitClient.getInstance(context)
    private val applicationPreference = ApplicationPreference.getInstance(context)

    val userInfoLiveData = MutableLiveData<User>()

    fun getUserInfo() {
        apiService
            .getUserInfo()
            .enqueue(object : Callback<BaseResponse<UserInfoResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<UserInfoResponse>>,
                    response: Response<BaseResponse<UserInfoResponse>>
                ) {
                    val userInfoResponse = response.body()?.data
                    val userInfo = User(
                        id = userInfoResponse?.id,
                        phoneNumber = userInfoResponse?.phoneNumber,
                        email = userInfoResponse?.email,
                        firstName = userInfoResponse?.firstName,
                        lastName = userInfoResponse?.lastName
                    )
                    applicationPreference?.saveUserInfo(userInfo)
                    userInfoLiveData.value = userInfo
                }

                override fun onFailure(call: Call<BaseResponse<UserInfoResponse>>, t: Throwable) {
                }

            })
    }
}