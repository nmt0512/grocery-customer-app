package com.example.grocerystoretest.model.response.bill

data class BillItemResponse(
    val productId: Int,
    val quantity: Int,
    val price: Int
)