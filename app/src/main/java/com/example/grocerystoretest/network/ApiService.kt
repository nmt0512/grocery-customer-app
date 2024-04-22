package com.example.grocerystoretest.network

import com.example.grocerystoretest.model.request.auth.ChangePasswordRequest
import com.example.grocerystoretest.model.request.auth.LoginRequest
import com.example.grocerystoretest.model.request.auth.RegisterCustomerRequest
import com.example.grocerystoretest.model.request.auth.UpdateUserInfoRequest
import com.example.grocerystoretest.model.request.bill.CreateBillRequest
import com.example.grocerystoretest.model.request.cart.AddToCartRequest
import com.example.grocerystoretest.model.request.cart.UpdateCartQuantityRequest
import com.example.grocerystoretest.model.request.customer_device.CreateCustomerDeviceRequest
import com.example.grocerystoretest.model.request.payment.StripeConfirmPaymentRequest
import com.example.grocerystoretest.model.request.product.GetAvailableProductListRequest
import com.example.grocerystoretest.model.request.product.GetAvailableProductListResponse
import com.example.grocerystoretest.model.response.BaseResponse
import com.example.grocerystoretest.model.response.auth.ChangePasswordResponse
import com.example.grocerystoretest.model.response.auth.LoginResponse
import com.example.grocerystoretest.model.response.auth.RegisterCustomerResponse
import com.example.grocerystoretest.model.response.auth.UserInfoResponse
import com.example.grocerystoretest.model.response.bill.CreateBillResponse
import com.example.grocerystoretest.model.response.bill.GetAllBillResponse
import com.example.grocerystoretest.model.response.cart.AddToCartResponse
import com.example.grocerystoretest.model.response.cart.DeleteCartByIdListResponse
import com.example.grocerystoretest.model.response.cart.GetAllItemInCartResponse
import com.example.grocerystoretest.model.response.cart.UpdateCartQuantityResponse
import com.example.grocerystoretest.model.response.category.CategoryListResponse
import com.example.grocerystoretest.model.response.customer_device.CreateCustomerDeviceResponse
import com.example.grocerystoretest.model.response.payment.StripeConfirmPaymentResponse
import com.example.grocerystoretest.model.response.product.BestSellingProductResponse
import com.example.grocerystoretest.model.response.product.ProductListResponse
import com.example.grocerystoretest.model.response.product.ProductResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<BaseResponse<LoginResponse>>

    @PUT("auth/password")
    fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Call<BaseResponse<ChangePasswordResponse>>

    @GET("auth/userInfo")
    fun getUserInfo(): Call<BaseResponse<UserInfoResponse>>

    @PUT("auth/userInfo")
    fun updateUserInfo(@Body updateUserInfoRequest: UpdateUserInfoRequest): Call<BaseResponse<UserInfoResponse>>

    @POST("auth/registerCustomer")
    fun registerCustomer(@Body registerCustomerRequest: RegisterCustomerRequest): Call<BaseResponse<RegisterCustomerResponse>>

    @POST("cart/add")
    fun addToCart(@Body addToCartRequest: AddToCartRequest): Call<BaseResponse<AddToCartResponse>>

    @GET("cart/all")
    fun getAllItemInCart(): Call<BaseResponse<GetAllItemInCartResponse>>

    @PUT("cart/quantity")
    fun updateCartQuantity(@Body updateCartQuantityRequest: UpdateCartQuantityRequest): Call<BaseResponse<UpdateCartQuantityResponse>>

    @DELETE("cart")
    fun deleteCartByIdList(@Query("idList") idList: List<String>): Call<BaseResponse<DeleteCartByIdListResponse>>

    @GET("category/all")
    fun getAllCategory(): Call<BaseResponse<CategoryListResponse>>

    @GET("product")
    fun getProductByCategoryIdPaging(
        @Query("categoryId") categoryId: Int,
        @Query("page") pageNumber: Int,
        @Query("size") pageSize: Int
    ): Call<BaseResponse<ProductListResponse>>

    @GET("product/{id}")
    fun getProductById(@Path("id") productId: Int): Call<BaseResponse<ProductResponse>>

    @POST("product/available")
    fun getAvailableProductList(@Body getAvailableProductListRequest: GetAvailableProductListRequest): Call<BaseResponse<GetAvailableProductListResponse>>

    @GET("product/search")
    fun searchProduct(@Query("query") query: String): Call<BaseResponse<ProductListResponse>>

    @GET("product/bestSelling")
    fun getBestSellingProduct(): Call<BaseResponse<BestSellingProductResponse>>

    @POST("payment/stripe")
    fun confirmPaymentStripe(@Body stripeConfirmPaymentRequest: StripeConfirmPaymentRequest): Call<BaseResponse<StripeConfirmPaymentResponse>>

    @POST("bill")
    fun createBill(@Body createBillRequest: CreateBillRequest): Call<BaseResponse<CreateBillResponse>>

    @GET("bill/all")
    fun getAllBill(
        @Query("status") billStatusList: List<String>,
        @Query("page") pageNumber: Int,
        @Query("size") pageSize: Int
    ): Call<BaseResponse<GetAllBillResponse>>

    @POST("device")
    fun createCustomerDevice(@Body createCustomerDeviceRequest: CreateCustomerDeviceRequest): Call<BaseResponse<CreateCustomerDeviceResponse>>
}