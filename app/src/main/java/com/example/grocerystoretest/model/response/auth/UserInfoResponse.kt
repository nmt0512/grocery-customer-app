package com.example.grocerystoretest.model.response.auth

data class UserInfoResponse(
    val id: String,
    val phoneNumber: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: String
)