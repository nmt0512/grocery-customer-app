package com.example.grocerystoretest.view

import com.example.grocerystoretest.model.response.product.ProductResponse

interface IProductListActivity {

    fun showBottomSheetDialog(productResponse: ProductResponse)
}