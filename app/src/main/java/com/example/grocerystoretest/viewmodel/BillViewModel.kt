package com.example.grocerystoretest.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grocerystoretest.enums.BillStatus
import com.example.grocerystoretest.model.request.bill.CreateBillRequest
import com.example.grocerystoretest.model.response.BaseResponse
import com.example.grocerystoretest.model.response.bill.BillResponse
import com.example.grocerystoretest.model.response.bill.CreateBillResponse
import com.example.grocerystoretest.model.response.bill.GetAllBillResponse
import com.example.grocerystoretest.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BillViewModel(context: Context) : ViewModel() {

    private val apiService = RetrofitClient.getInstance(context)

    fun createBill(createBillRequest: CreateBillRequest): MutableLiveData<BillResponse> {
        val billResponseLiveData = MutableLiveData<BillResponse>()
        apiService
            .createBill(createBillRequest)
            .enqueue(object : Callback<BaseResponse<CreateBillResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<CreateBillResponse>>,
                    response: Response<BaseResponse<CreateBillResponse>>
                ) {
                    if (response.body() != null) {
                        billResponseLiveData.value = response.body()!!.data!!.billResponse
                    } else {
                        billResponseLiveData.value = BillResponse()
                    }

                }

                override fun onFailure(call: Call<BaseResponse<CreateBillResponse>>, t: Throwable) {
                    billResponseLiveData.value = BillResponse()
                }

            })
        return billResponseLiveData
    }

    fun getAllBill(billStatus: BillStatus): MutableLiveData<List<BillResponse>> {
        val billResponseListLiveData = MutableLiveData<List<BillResponse>>()
        apiService
            .getAllBill(billStatus)
            .enqueue(object : Callback<BaseResponse<GetAllBillResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<GetAllBillResponse>>,
                    response: Response<BaseResponse<GetAllBillResponse>>
                ) {
                    if (response.body() != null) {
                        billResponseListLiveData.value = response.body()!!.data?.billResponseList
                    } else {
                        billResponseListLiveData.value = mutableListOf()
                    }
                }

                override fun onFailure(call: Call<BaseResponse<GetAllBillResponse>>, t: Throwable) {
                    billResponseListLiveData.value = mutableListOf()
                }

            })
        return billResponseListLiveData
    }

}