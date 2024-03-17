package com.example.grocerystoretest.model.request.product

data class AvailableProductRequest(
    val productId: Int,
    val quantity: Int
)