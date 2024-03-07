package com.example.grocerystoretest.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grocerystoretest.model.response.BaseResponse
import com.example.grocerystoretest.model.response.product.ProductListResponse
import com.example.grocerystoretest.model.response.product.ProductResponse
import com.example.grocerystoretest.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel(context: Context) : ViewModel() {

    private val apiService = RetrofitClient.getInstance(context)

    fun getProductResponseListByCategoryIdPaging(
        categoryId: Int,
        pageNumber: Int,
        pageSize: Int
    ): MutableLiveData<List<ProductResponse>> {
        val productResponseListLiveData = MutableLiveData<List<ProductResponse>>()
        apiService
            .getProductByCategoryIdPaging(categoryId, pageNumber, pageSize)
            .enqueue(object : Callback<BaseResponse<ProductListResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<ProductListResponse>>,
                    response: Response<BaseResponse<ProductListResponse>>
                ) {
                    productResponseListLiveData.value = response.body()?.data?.content
                }

                override fun onFailure(
                    call: Call<BaseResponse<ProductListResponse>>,
                    t: Throwable
                ) {
                }
            })
        return productResponseListLiveData
    }
}