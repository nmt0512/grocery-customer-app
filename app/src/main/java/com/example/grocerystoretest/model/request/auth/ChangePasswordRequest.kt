package com.example.grocerystoretest.model.request.auth

data class ChangePasswordRequest(
    private val oldPassword: String,
    private val newPassword: String
)