package com.example.grocerystoretest.model.response.bill

import com.example.grocerystoretest.model.response.product.ProductResponse

data class BillItemResponse(
    val productId: Int,
    val productResponse: ProductResponse = ProductResponse(),
    val quantity: Int,
    val price: Int
)