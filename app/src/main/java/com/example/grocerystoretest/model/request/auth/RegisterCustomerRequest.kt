package com.example.grocerystoretest.model.request.auth

data class RegisterCustomerRequest(
    private val phoneNumber: String,
    private val password: String,
    private val firstName: String,
    private val lastName: String,
    private val email: String
)
