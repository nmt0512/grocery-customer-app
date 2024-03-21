package com.example.grocerystoretest.model.response

data class BaseResponse<T>(
    val success: Boolean? = false,
    val code: Int? = 0,
    val data: T?
)
