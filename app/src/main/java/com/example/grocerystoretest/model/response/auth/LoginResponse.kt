package com.example.grocerystoretest.model.response.auth

data class LoginResponse(
    val refreshToken: String,
    val refreshExpiresIn: Int,
    val accessToken: String,
    val expiresIn: Int,
    val tokenType: String
)