package com.example.grocerystoretest.model.response.product

data class ProductResponse(
    val id: Int = 0,
    val name: String = "",
    val code: String = "",
    val description: String = "",
    val unitPrice: Int = 0,
    val quantity: Int = 0,
    val images: MutableList<String> = mutableListOf()
)