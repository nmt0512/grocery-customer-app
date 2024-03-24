package com.example.grocerystoretest.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grocerystoretest.model.request.product.GetAvailableProductListRequest
import com.example.grocerystoretest.model.request.product.GetAvailableProductListResponse
import com.example.grocerystoretest.model.response.BaseResponse
import com.example.grocerystoretest.model.response.product.ProductListResponse
import com.example.grocerystoretest.model.response.product.ProductResponse
import com.example.grocerystoretest.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel(context: Context) : ViewModel() {

    private val apiService = RetrofitClient.getInstance(context)
    val productDetailLiveData = MutableLiveData<ProductResponse>()
    val searchProductResponseListLiveData = MutableLiveData<List<ProductResponse>>()

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

    fun getProductDetailById(productId: Int) {
        apiService
            .getProductById(productId)
            .enqueue(object : Callback<BaseResponse<ProductResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<ProductResponse>>,
                    response: Response<BaseResponse<ProductResponse>>
                ) {
                    productDetailLiveData.value = response.body()?.data!!
                }

                override fun onFailure(call: Call<BaseResponse<ProductResponse>>, t: Throwable) {

                }

            })
    }

    fun getAvailableProductList(getAvailableProductListRequest: GetAvailableProductListRequest): MutableLiveData<GetAvailableProductListResponse> {
        val getAvailableProductListResponseLiveData =
            MutableLiveData<GetAvailableProductListResponse>()
        apiService
            .getAvailableProductList(getAvailableProductListRequest)
            .enqueue(object : Callback<BaseResponse<GetAvailableProductListResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<GetAvailableProductListResponse>>,
                    response: Response<BaseResponse<GetAvailableProductListResponse>>
                ) {
                    getAvailableProductListResponseLiveData.value = response.body()?.data!!
                }

                override fun onFailure(
                    call: Call<BaseResponse<GetAvailableProductListResponse>>,
                    t: Throwable
                ) {

                }

            })
        return getAvailableProductListResponseLiveData
    }

    fun searchProduct(query: String) {
        apiService
            .searchProduct(query)
            .enqueue(object : Callback<BaseResponse<ProductListResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<ProductListResponse>>,
                    response: Response<BaseResponse<ProductListResponse>>
                ) {
                    if (response.body() != null) {
                        searchProductResponseListLiveData.value = response.body()!!.data?.content
                    } else {
                        searchProductResponseListLiveData.value = mutableListOf()
                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<ProductListResponse>>,
                    t: Throwable
                ) {
                    searchProductResponseListLiveData.value = mutableListOf()
                }

            })
    }
}