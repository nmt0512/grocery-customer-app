package com.example.grocerystoretest.model.response.cart

data class AddToCartResponse(
    val id: String? = "",
    val productId: Int? = 0,
    val quantity: Int? = 0,
    val totalPrice: Int? = 0
)