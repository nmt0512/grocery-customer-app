package com.example.grocerystoretest.enums

enum class PaymentMethodEnum(
    val shortName: String,
    val longName: String
) {
    NONE("none", "none"),
    STRIPE("Stripe", "Cổng thanh toán quốc tế Stripe")

}