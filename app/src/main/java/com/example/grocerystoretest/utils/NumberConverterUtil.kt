package com.example.grocerystoretest.utils

object NumberConverterUtil {
    fun convertNumberToStringWithDot(number: Int): String {
        val formattedNumber = StringBuilder(number.toString())
        var index = formattedNumber.length - 3

        while (index > 0) {
            formattedNumber.insert(index, '.')
            index -= 3
        }

        return formattedNumber.toString()
    }

    fun convertStringWithDotToNumber(stringWithDot: String): Int {
        val cleanedString = stringWithDot.replace("[,.\\s]".toRegex(), "")
        return cleanedString.toIntOrNull() ?: 0
    }

    fun getProductPriceStringByPrice(price: Int): String {
        return "Giá: ${convertNumberToStringWithDot(price)} Đ"
    }
}