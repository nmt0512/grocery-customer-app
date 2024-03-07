package com.example.grocerystoretest.network

import com.example.grocerystoretest.model.request.auth.LoginRequest
import com.example.grocerystoretest.model.request.cart.AddToCartRequest
import com.example.grocerystoretest.model.request.cart.UpdateCartQuantityRequest
import com.example.grocerystoretest.model.response.BaseResponse
import com.example.grocerystoretest.model.response.auth.LoginResponse
import com.example.grocerystoretest.model.response.auth.UserInfoResponse
import com.example.grocerystoretest.model.response.cart.AddToCartResponse
import com.example.grocerystoretest.model.response.cart.DeleteCartByIdListResponse
import com.example.grocerystoretest.model.response.cart.GetAllItemInCartResponse
import com.example.grocerystoretest.model.response.cart.UpdateCartQuantityResponse
import com.example.grocerystoretest.model.response.category.CategoryListResponse
import com.example.grocerystoretest.model.response.product.ProductListResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<BaseResponse<LoginResponse>>

    @GET("userInfo")
    fun getUserInfo(): Call<BaseResponse<UserInfoResponse>>

    @POST("cart/add")
    fun addToCart(@Body addToCartRequest: AddToCartRequest): Call<BaseResponse<AddToCartResponse>>

    @GET("cart/all")
    fun getAllItemInCart(): Call<BaseResponse<GetAllItemInCartResponse>>

    @PUT("cart/quantity")
    fun updateCartQuantity(@Body updateCartQuantityRequest: UpdateCartQuantityRequest): Call<BaseResponse<UpdateCartQuantityResponse>>

    @DELETE("cart")
    fun deleteCartByIdList(@Query("idList") idList: List<String>): Call<BaseResponse<DeleteCartByIdListResponse>>

}