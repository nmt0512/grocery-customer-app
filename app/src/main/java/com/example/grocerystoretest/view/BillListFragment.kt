package com.example.grocerystoretest.view

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerystoretest.R
import com.example.grocerystoretest.adapter.RecyclerViewBillListAdapter
import com.example.grocerystoretest.base.BaseFragment
import com.example.grocerystoretest.databinding.FragmentBillListBinding
import com.example.grocerystoretest.enums.BillStatus
import com.example.grocerystoretest.viewmodel.BillViewModel

class BillListFragment(private val billStatusList: List<BillStatus>) :
    BaseFragment<FragmentBillListBinding>() {

    private lateinit var billViewModel: BillViewModel

    override fun getContentLayout(): Int {
        return R.layout.fragment_bill_list
    }

    override fun initView() {
        billViewModel = BillViewModel(this.requireContext())
        loadingDialog?.show()
    }

    override fun onResume() {
        observeLiveData()
        super.onResume()
    }

    override fun initListener() {

    }

    override fun observeLiveData() {
        binding.rvBillList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        billViewModel.getAllBill(billStatusList).observe(this) {
            binding.rvBillList.adapter = RecyclerViewBillListAdapter(it)
            loadingDialog?.dismiss()
        }
    }

}