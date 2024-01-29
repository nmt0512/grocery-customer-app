package com.example.grocerystoretest.network

import com.example.grocerystoretest.dto.response.BaseResponse
import com.example.grocerystoretest.dto.response.category.CategoryListResponse
import com.example.grocerystoretest.dto.response.product.ProductListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("category/all")
    fun getAllCategory(): Call<BaseResponse<CategoryListResponse>>

    @GET("product")
    fun getProductByCategoryIdPaging(
        @Query("categoryId") categoryId: Int,
        @Query("page") pageNumber: Int,
        @Query("size") pageSize: Int
    ): Call<BaseResponse<ProductListResponse>>
}