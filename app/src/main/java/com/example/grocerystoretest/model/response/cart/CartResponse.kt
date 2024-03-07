package com.example.grocerystoretest.model.response.cart

import com.example.grocerystoretest.model.response.product.ProductResponse

data class CartResponse(
    val id: String,
    val product: ProductResponse,
    var quantity: Int
)