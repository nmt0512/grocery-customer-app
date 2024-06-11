package com.example.grocerystoretest.model.response.payment

data class StripeConfirmPaymentResponse(
    val paymentIntentId: String,
    val paymentIntentClientSecret: String,
    val ephemeralKey: String,
    val customer: String,
    val publishableKey: String
)