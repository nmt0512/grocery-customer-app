package com.example.grocerystoretest.dto.response.product

data class ProductResponse(
    val id: Int,
    val name: String,
    val code: String,
    val description: String,
    val unitPrice: Int,
    val images: MutableList<String>
)