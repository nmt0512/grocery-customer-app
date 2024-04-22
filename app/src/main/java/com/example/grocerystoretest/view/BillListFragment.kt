package com.example.grocerystoretest.view

import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerystoretest.R
import com.example.grocerystoretest.adapter.RecyclerViewBillListAdapter
import com.example.grocerystoretest.base.BaseFragment
import com.example.grocerystoretest.databinding.FragmentBillListBinding
import com.example.grocerystoretest.enums.BillStatus
import com.example.grocerystoretest.model.response.bill.BillResponse
import com.example.grocerystoretest.viewmodel.BillViewModel

class BillListFragment(private val billStatusList: List<BillStatus>) :
    BaseFragment<FragmentBillListBinding>() {

    private lateinit var billViewModel: BillViewModel

    private val billResponseList = mutableListOf<BillResponse>()

    private val pageSize = 8
    private var pageNumber = 1

    private var isAbleToFetchMore = true
    private var isFetchedFirstPage = false

    override fun getContentLayout(): Int {
        return R.layout.fragment_bill_list
    }

    override fun initView() {
        loadingDialog?.dismiss()
        billViewModel = BillViewModel(this.requireContext())
        binding.rvBillList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onResume() {
        super.onResume()
        billResponseList.clear()
        binding.rvBillList.adapter?.notifyDataSetChanged()
        binding.pbLoading.visibility = View.VISIBLE
        pageNumber = 1
        isAbleToFetchMore = true
        isFetchedFirstPage = false
        observeLiveData()
    }

    override fun initListener() {
        binding.nsvRvBill.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (isAbleToFetchMore && scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                isAbleToFetchMore = false
                pageNumber++
                binding.pbLoading.visibility = View.VISIBLE
                observeLiveData()
            }
        })
    }

    override fun observeLiveData() {
        billViewModel.getAllBill(
            billStatusList,
            pageNumber,
            pageSize
        ).observe(this) {
            if (it != null && !(isFetchedFirstPage && pageNumber == 1)) {
                if (it.isNotEmpty()) {
                    billResponseList.addAll(it)

                    if (pageNumber == 1) {
                        isFetchedFirstPage = true
                    }

                    if (it.size < pageSize) {
                        disableFetchMore()
                    } else {
                        isAbleToFetchMore = true
                    }
                } else {
                    disableFetchMore()
                }
            }

            binding.rvBillList.adapter =
                RecyclerViewBillListAdapter(billResponseList)

            loadingDialog?.dismiss()
        }
    }

    private fun disableFetchMore() {
        isAbleToFetchMore = false
        binding.pbLoading.visibility = View.GONE
    }

}