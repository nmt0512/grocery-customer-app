package com.example.grocerystoretest.model.response

data class BaseResponse<T>(
    val success: Boolean,
    val code: Int,
    val data: T?
)
