package com.example.grocerystoretest.model.response.cart

import com.example.grocerystoretest.model.response.product.ProductResponse
import java.io.Serializable

data class CartResponse(
    val id: String,
    val product: ProductResponse,
    var quantity: Int
) : Serializable