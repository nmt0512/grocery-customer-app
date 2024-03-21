package com.example.grocerystoretest.model.request.auth

data class UpdateUserInfoRequest(
    private val firstName: String,
    private val lastName: String,
    private val email: String
)