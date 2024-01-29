package com.example.grocerystoretest.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grocerystoretest.dto.response.BaseResponse
import com.example.grocerystoretest.dto.response.category.CategoryListResponse
import com.example.grocerystoretest.model.Category
import com.example.grocerystoretest.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryViewModel : ViewModel() {
    var categoryListLiveData = MutableLiveData<List<Category>>()

    fun getCategoryResponseList(): MutableLiveData<List<Category>> {
        RetrofitClient.instance
            .getAllCategory()
            .enqueue(object : Callback<BaseResponse<CategoryListResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<CategoryListResponse>>,
                    response: Response<BaseResponse<CategoryListResponse>>
                ) {
                    categoryListLiveData.value = response
                        .body()?.data?.categoryList?.map { categoryResponse ->
                            Category(
                                categoryResponse.id,
                                categoryResponse.imageUrl,
                                categoryResponse.name
                            )
                        }
                }

                override fun onFailure(
                    call: Call<BaseResponse<CategoryListResponse>>,
                    t: Throwable
                ) {
                }
            })
        return categoryListLiveData
    }

}