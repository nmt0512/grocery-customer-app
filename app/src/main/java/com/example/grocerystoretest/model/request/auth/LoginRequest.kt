package com.example.grocerystoretest.model.request.auth

data class LoginRequest(val phoneNumber: String? = "", val password: String? = "")