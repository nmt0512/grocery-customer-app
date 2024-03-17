package com.example.grocerystoretest.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grocerystoretest.model.request.payment.StripeConfirmPaymentRequest
import com.example.grocerystoretest.model.response.BaseResponse
import com.example.grocerystoretest.model.response.payment.StripeConfirmPaymentResponse
import com.example.grocerystoretest.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentViewModel(context: Context): ViewModel() {

    private val apiService = RetrofitClient.getInstance(context)
    val confirmPaymentResponseLiveData = MutableLiveData<StripeConfirmPaymentResponse>()

    fun confirmPaymentStripe(totalPrice: Int) {
        val stripeConfirmPaymentRequest = StripeConfirmPaymentRequest(totalPrice)
        apiService
            .confirmPaymentStripe(stripeConfirmPaymentRequest)
            .enqueue(object : Callback<BaseResponse<StripeConfirmPaymentResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<StripeConfirmPaymentResponse>>,
                    response: Response<BaseResponse<StripeConfirmPaymentResponse>>
                ) {
                    confirmPaymentResponseLiveData.value = response.body()?.data!!
                }

                override fun onFailure(
                    call: Call<BaseResponse<StripeConfirmPaymentResponse>>,
                    t: Throwable
                ) {

                }

            })
    }
}