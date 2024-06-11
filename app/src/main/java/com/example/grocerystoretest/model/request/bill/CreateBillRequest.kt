package com.example.grocerystoretest.model.request.bill

data class CreateBillRequest(
    val paymentIntentId: String,
    val cartIdList: List<String>,
    val pickUpDate: String,
    val pickUpTime: String
)