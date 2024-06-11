package com.example.grocerystoretest.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.grocerystoretest.model.request.cart.AddToCartRequest
import com.example.grocerystoretest.model.request.cart.UpdateCartQuantityRequest
import com.example.grocerystoretest.model.response.BaseResponse
import com.example.grocerystoretest.model.response.cart.AddToCartResponse
import com.example.grocerystoretest.model.response.cart.CartResponse
import com.example.grocerystoretest.model.response.cart.DeleteCartByIdListResponse
import com.example.grocerystoretest.model.response.cart.GetAllItemInCartResponse
import com.example.grocerystoretest.model.response.cart.UpdateCartQuantityResponse
import com.example.grocerystoretest.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartViewModel(context: Context) {

    private val apiService = RetrofitClient.getInstance(context)
    val addToCartResponseLiveData = MutableLiveData<AddToCartResponse>()
    val cartResponseListLiveData = MutableLiveData<List<CartResponse>>()

    fun addToCart(addToCartRequest: AddToCartRequest) {
        apiService
            .addToCart(addToCartRequest)
            .enqueue(object : Callback<BaseResponse<AddToCartResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<AddToCartResponse>>,
                    response: Response<BaseResponse<AddToCartResponse>>
                ) {
                    addToCartResponseLiveData.value = response.body()?.data ?: AddToCartResponse()
                }

                override fun onFailure(call: Call<BaseResponse<AddToCartResponse>>, t: Throwable) {
                    addToCartResponseLiveData.value = AddToCartResponse()
                }

            })
    }

    fun getAllItemInCart() {
        apiService
            .getAllItemInCart()
            .enqueue(object : Callback<BaseResponse<GetAllItemInCartResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<GetAllItemInCartResponse>>,
                    response: Response<BaseResponse<GetAllItemInCartResponse>>
                ) {
                    cartResponseListLiveData.value =
                        response.body()?.data?.cartResponseList ?: mutableListOf()
                }

                override fun onFailure(
                    call: Call<BaseResponse<GetAllItemInCartResponse>>,
                    t: Throwable
                ) {
                    cartResponseListLiveData.value = mutableListOf()
                }

            })
    }

    fun updateCartQuantity(updateCartQuantityRequest: UpdateCartQuantityRequest): MutableLiveData<CartResponse> {
        val cartResponseLiveData = MutableLiveData<CartResponse>()
        apiService
            .updateCartQuantity(updateCartQuantityRequest)
            .enqueue(object : Callback<BaseResponse<UpdateCartQuantityResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<UpdateCartQuantityResponse>>,
                    response: Response<BaseResponse<UpdateCartQuantityResponse>>
                ) {
                    cartResponseLiveData.value = response.body()?.data?.cartResponse
                }

                override fun onFailure(
                    call: Call<BaseResponse<UpdateCartQuantityResponse>>,
                    t: Throwable
                ) {

                }
            })
        return cartResponseLiveData
    }

    fun deleteCartByIdList(idList: List<String>): MutableLiveData<DeleteCartByIdListResponse> {
        val deleteCartResponseLiveData = MutableLiveData<DeleteCartByIdListResponse>()
        apiService
            .deleteCartByIdList(idList)
            .enqueue(object : Callback<BaseResponse<DeleteCartByIdListResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<DeleteCartByIdListResponse>>,
                    response: Response<BaseResponse<DeleteCartByIdListResponse>>
                ) {
                    deleteCartResponseLiveData.value = response.body()?.data!!
                }

                override fun onFailure(
                    call: Call<BaseResponse<DeleteCartByIdListResponse>>,
                    t: Throwable
                ) {

                }

            })
        return deleteCartResponseLiveData
    }
}