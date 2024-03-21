package com.example.grocerystoretest.view

import com.example.grocerystoretest.R
import com.example.grocerystoretest.adapter.BillViewPagerAdapter
import com.example.grocerystoretest.base.BaseFragment
import com.example.grocerystoretest.databinding.FragmentBillBinding
import com.google.android.material.tabs.TabLayoutMediator

class BillFragment : BaseFragment<FragmentBillBinding>() {

    override fun getContentLayout(): Int {
        return R.layout.fragment_bill
    }

    override fun initView() {
        binding.viewPager.adapter = BillViewPagerAdapter(this.requireActivity())
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            if (position == 0) {
                tab.text = "Đang chờ"
            } else if (position == 1) {
                tab.text = "Đã hoàn thành"
            }
        }.attach()
    }

    override fun initListener() {

    }

    override fun observeLiveData() {

    }

}