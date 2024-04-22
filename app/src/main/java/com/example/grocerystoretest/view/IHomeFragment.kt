package com.example.grocerystoretest.view

import com.example.grocerystoretest.model.response.product.ProductResponse

interface IHomeFragment {

    fun showBottomSheetDialog(productResponse: ProductResponse)
}