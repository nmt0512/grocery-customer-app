package com.example.grocerystoretest.enums

enum class BillStatus(val description: String) {
    PAID("Đã thanh toán"),
    COMPLETED("Đã hoàn thành"),
    CANCELLED("Đã hủy")
}