package com.example.grocerystoretest.model.request.bill

data class CreateBillRequest(
    val cartIdList: List<String>
)