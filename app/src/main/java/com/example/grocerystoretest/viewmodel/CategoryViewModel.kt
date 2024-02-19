package com.example.grocerystoretest.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grocerystoretest.model.response.BaseResponse
import com.example.grocerystoretest.model.response.category.CategoryListResponse
import com.example.grocerystoretest.model.response.category.CategoryResponse
import com.example.grocerystoretest.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryViewModel : ViewModel() {

    private val categoryResponseListLiveData = MutableLiveData<List<CategoryResponse>>()

    fun getCategoryResponseList(): MutableLiveData<List<CategoryResponse>> {
        RetrofitClient.instance
            .getAllCategory()
            .enqueue(object : Callback<BaseResponse<CategoryListResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<CategoryListResponse>>,
                    response: Response<BaseResponse<CategoryListResponse>>
                ) {
                    categoryResponseListLiveData.value = response.body()?.data?.categoryList
                }

                override fun onFailure(
                    call: Call<BaseResponse<CategoryListResponse>>,
                    t: Throwable
                ) {
                }
            })
        return categoryResponseListLiveData
    }

}