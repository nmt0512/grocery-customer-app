package com.example.grocerystoretest.model.response.bill

data class BillResponse(
    val id: Int? = 0,
    val totalPrice: Int? = 0,
    val customerId: String? = "",
    val createdDate: String? = "",
    val billItems: List<BillItemResponse>? = mutableListOf()
)