package com.example.grocerystoretest.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grocerystoretest.model.request.bill.CreateBillRequest
import com.example.grocerystoretest.model.response.BaseResponse
import com.example.grocerystoretest.model.response.bill.BillResponse
import com.example.grocerystoretest.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BillViewModel(context: Context) : ViewModel() {

    private val apiService = RetrofitClient.getInstance(context)

    fun createBill(cartIdList: List<String>): MutableLiveData<BillResponse> {
        val billResponseLiveData = MutableLiveData<BillResponse>()
        apiService
            .createBill(CreateBillRequest(cartIdList))
            .enqueue(object : Callback<BaseResponse<BillResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<BillResponse>>,
                    response: Response<BaseResponse<BillResponse>>
                ) {
                    if (response.body() != null) {
                        billResponseLiveData.value = response.body()!!.data!!
                    } else {
                        billResponseLiveData.value = BillResponse()
                    }

                }

                override fun onFailure(call: Call<BaseResponse<BillResponse>>, t: Throwable) {
                    billResponseLiveData.value = BillResponse()
                }

            })
        return billResponseLiveData
    }

}