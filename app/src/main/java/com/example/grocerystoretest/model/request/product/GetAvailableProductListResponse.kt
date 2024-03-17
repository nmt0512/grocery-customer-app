package com.example.grocerystoretest.model.request.product

import com.example.grocerystoretest.model.response.product.ProductResponse

data class GetAvailableProductListResponse(
    val isAvailable: Boolean,
    val unavailableProductResponseList: List<ProductResponse>? = mutableListOf()
)