package com.example.grocerystoretest.model.response.customer_device

data class CreateCustomerDeviceResponse(
    val id: Int,
    val deviceId: String,
    val deviceToken: String
)
