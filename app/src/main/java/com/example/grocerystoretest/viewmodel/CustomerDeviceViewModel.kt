package com.example.grocerystoretest.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grocerystoretest.model.request.customer_device.CreateCustomerDeviceRequest
import com.example.grocerystoretest.model.response.BaseResponse
import com.example.grocerystoretest.model.response.customer_device.CreateCustomerDeviceResponse
import com.example.grocerystoretest.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerDeviceViewModel(context: Context) : ViewModel() {

    private val apiService = RetrofitClient.getInstance(context)

    fun createCustomerDevice(createCustomerDeviceRequest: CreateCustomerDeviceRequest): MutableLiveData<Boolean> {
        val isCreatedSuccessLiveData = MutableLiveData<Boolean>()
        apiService
            .createCustomerDevice(createCustomerDeviceRequest)
            .enqueue(object : Callback<BaseResponse<CreateCustomerDeviceResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<CreateCustomerDeviceResponse>>,
                    response: Response<BaseResponse<CreateCustomerDeviceResponse>>
                ) {
                    isCreatedSuccessLiveData.value = response.body() != null
                }

                override fun onFailure(
                    call: Call<BaseResponse<CreateCustomerDeviceResponse>>,
                    t: Throwable
                ) {
                    isCreatedSuccessLiveData.value = false
                }

            })
        return isCreatedSuccessLiveData
    }
}