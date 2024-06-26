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
        binding.viewPager.isUserInputEnabled = false
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Đang chờ"
                }

                1 -> {
                    tab.text = "Đã chuẩn bị"
                }

                2 -> {
                    tab.text = "Đã hoàn thành"
                }

                3 -> {
                    tab.text = "Đã hủy"
                }
            }
        }.attach()
    }

    override fun initListener() {

    }

    override fun observeLiveData() {

    }

}