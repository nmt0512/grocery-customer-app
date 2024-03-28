package com.example.grocerystoretest.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.grocerystoretest.enums.BillStatus
import com.example.grocerystoretest.view.BillListFragment

class BillViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            BillListFragment(mutableListOf(BillStatus.PAID, BillStatus.PREPARED))
        } else {
            BillListFragment(mutableListOf(BillStatus.COMPLETED))
        }
    }
}