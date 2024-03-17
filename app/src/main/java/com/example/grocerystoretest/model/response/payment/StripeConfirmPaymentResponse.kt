package com.example.grocerystoretest.model.response.payment

data class StripeConfirmPaymentResponse(
    val paymentIntent: String,
    val ephemeralKey: String,
    val customer: String,
    val publishableKey: String
)