package com.example.grocerystoretest.view

import com.example.grocerystoretest.model.request.cart.UpdateCartQuantityRequest

interface ICartFragment {

    fun addToCheckedCartItemSet(cartId: String)

    fun removeFromCheckedCartItemSet(cartId: String)

    fun isCheckedCartItemSetContains(cartId: String): Boolean

    fun plusTotalPrice(inputMoney: Int)

    fun minusTotalPrice(inputMoney: Int)

    fun updateCartQuantity(
        position: Int,
        updateCartQuantityRequest: UpdateCartQuantityRequest,
        isPlusTotalPrice: Boolean?,
        updatingMoney: Int?
    )
}