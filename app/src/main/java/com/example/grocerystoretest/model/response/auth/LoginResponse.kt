package com.example.grocerystoretest.model.response.auth

data class LoginResponse(
    val accessToken: String,
    val expiresIn: Int,
    val tokenType: String
)