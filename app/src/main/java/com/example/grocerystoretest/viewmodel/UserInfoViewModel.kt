package com.example.grocerystoretest.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grocerystoretest.model.User
import com.example.grocerystoretest.model.request.auth.ChangePasswordRequest
import com.example.grocerystoretest.model.request.auth.RegisterCustomerRequest
import com.example.grocerystoretest.model.request.auth.UpdateUserInfoRequest
import com.example.grocerystoretest.model.response.BaseResponse
import com.example.grocerystoretest.model.response.auth.ChangePasswordResponse
import com.example.grocerystoretest.model.response.auth.RegisterCustomerResponse
import com.example.grocerystoretest.model.response.auth.UserInfoResponse
import com.example.grocerystoretest.network.RetrofitClient
import com.example.grocerystoretest.utils.ApplicationPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInfoViewModel(context: Context) : ViewModel() {

    private val apiService = RetrofitClient.getInstance(context)
    private val applicationPreference = ApplicationPreference.getInstance(context)

    fun getUserInfo(): MutableLiveData<User> {
        val userInfoLiveData = MutableLiveData<User>()
        apiService
            .getUserInfo()
            .enqueue(object : Callback<BaseResponse<UserInfoResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<UserInfoResponse>>,
                    response: Response<BaseResponse<UserInfoResponse>>
                ) {
                    response.body()?.let {
                        val userInfoResponse = response.body()!!.data
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
                }

                override fun onFailure(call: Call<BaseResponse<UserInfoResponse>>, t: Throwable) {
                }

            })
        return userInfoLiveData
    }

    fun registerCustomer(registerCustomerRequest: RegisterCustomerRequest): MutableLiveData<String> {
        val phoneNumberLiveData = MutableLiveData<String>()
        apiService
            .registerCustomer(registerCustomerRequest)
            .enqueue(object : Callback<BaseResponse<RegisterCustomerResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<RegisterCustomerResponse>>,
                    response: Response<BaseResponse<RegisterCustomerResponse>>
                ) {
                    phoneNumberLiveData.value = response.body()?.data?.registeredPhoneNumber ?: ""
                }

                override fun onFailure(
                    call: Call<BaseResponse<RegisterCustomerResponse>>,
                    t: Throwable
                ) {
                    phoneNumberLiveData.value = ""
                }

            })
        return phoneNumberLiveData
    }

    fun updateUserInfo(updateUserInfoRequest: UpdateUserInfoRequest): MutableLiveData<UserInfoResponse> {
        val userInfoResponseLiveData = MutableLiveData<UserInfoResponse>()
        apiService
            .updateUserInfo(updateUserInfoRequest)
            .enqueue(object : Callback<BaseResponse<UserInfoResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<UserInfoResponse>>,
                    response: Response<BaseResponse<UserInfoResponse>>
                ) {
                    userInfoResponseLiveData.value = response.body()?.data ?: UserInfoResponse()
                }

                override fun onFailure(call: Call<BaseResponse<UserInfoResponse>>, t: Throwable) {
                    userInfoResponseLiveData.value = UserInfoResponse()
                }

            })
        return userInfoResponseLiveData
    }

    fun changePassword(changePasswordRequest: ChangePasswordRequest): MutableLiveData<Boolean> {
        val isSuccessLiveData = MutableLiveData<Boolean>()
        apiService
            .changePassword(changePasswordRequest)
            .enqueue(object : Callback<BaseResponse<ChangePasswordResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<ChangePasswordResponse>>,
                    response: Response<BaseResponse<ChangePasswordResponse>>
                ) {
                    isSuccessLiveData.value = response.body() != null
                }

                override fun onFailure(
                    call: Call<BaseResponse<ChangePasswordResponse>>,
                    t: Throwable
                ) {
                    isSuccessLiveData.value = false
                }

            })
        return isSuccessLiveData
    }
}