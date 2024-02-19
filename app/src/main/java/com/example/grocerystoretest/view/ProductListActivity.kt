package com.example.grocerystoretest.view

import android.view.View
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.grocerystoretest.R
import com.example.grocerystoretest.adapter.RecyclerViewProductAdapter
import com.example.grocerystoretest.base.BaseActivity
import com.example.grocerystoretest.databinding.ActivityProductListBinding
import com.example.grocerystoretest.model.response.BaseResponse
import com.example.grocerystoretest.model.response.product.ProductListResponse
import com.example.grocerystoretest.model.response.product.ProductResponse
import com.example.grocerystoretest.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductListActivity : BaseActivity<ActivityProductListBinding>() {

    private val productPagingResponseList = mutableListOf<ProductResponse>()

    private var categoryId = 0
    private val pageSize = 8
    private var pageNumber = 1

    private var isAbleToFetchMore = true

    override fun getContentLayout(): Int {
        return R.layout.activity_product_list
    }

    override fun initView() {
        binding.rvProduct.layoutManager = GridLayoutManager(this, 2)

        categoryId = intent.getIntExtra("categoryId", 0)
        if (categoryId != 0) {
            binding.pbLoading.visibility = View.INVISIBLE
            loadingDialog?.show()
            getProductByCategoryIdPaging()
        }
    }

    override fun initListener() {
        binding.nsRvProduct.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (isAbleToFetchMore) {
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                    pageNumber++
                    binding.pbLoading.visibility = View.VISIBLE
                    getProductByCategoryIdPaging()
                }
            }
        })
    }

    private fun getProductByCategoryIdPaging() {
        if (categoryId != 0) {
            RetrofitClient.instance
                .getProductByCategoryIdPaging(categoryId, pageNumber, pageSize)
                .enqueue(object : Callback<BaseResponse<ProductListResponse>> {
                    override fun onResponse(
                        call: Call<BaseResponse<ProductListResponse>>,
                        response: Response<BaseResponse<ProductListResponse>>
                    ) {
                        response
                            .body()
                            ?.data
                            ?.content?.let {
                                if (it.isNotEmpty()) {
                                    productPagingResponseList.addAll(it)
                                    binding.rvProduct.adapter = RecyclerViewProductAdapter(
                                        this@ProductListActivity,
                                        productPagingResponseList
                                    )
                                    loadingDialog?.hide()
                                    if (it.size < pageSize) {
                                        disableFetchMore()
                                    }
                                } else {
                                    disableFetchMore()
                                }
                            }

                    }

                    override fun onFailure(
                        call: Call<BaseResponse<ProductListResponse>>,
                        t: Throwable
                    ) {
                        Toast.makeText(
                            this@ProductListActivity,
                            "Fail to fetch data",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                    fun disableFetchMore() {
                        isAbleToFetchMore = false
                        binding.pbLoading.visibility = View.GONE
                    }
                })
        }

    }

}