package com.example.grocerystoretest.model.response.product

data class ProductResponse(
    val id: Int,
    val name: String,
    val code: String,
    val description: String,
    val unitPrice: Int,
    val quantity: Int,
    val images: MutableList<String>
)