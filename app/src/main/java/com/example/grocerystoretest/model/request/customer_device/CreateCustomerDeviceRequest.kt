package com.example.grocerystoretest.model.request.customer_device

data class CreateCustomerDeviceRequest(
    val deviceId: String,
    val deviceToken: String
)