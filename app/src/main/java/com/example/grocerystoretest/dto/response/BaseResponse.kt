package com.example.grocerystoretest.dto.response

data class BaseResponse<T>(
    val success: Boolean,
    val code: Int,
    val data: T?
)
