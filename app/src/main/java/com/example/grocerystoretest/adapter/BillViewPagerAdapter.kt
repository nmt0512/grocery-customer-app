package com.example.grocerystoretest.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.grocerystoretest.enums.BillStatus
import com.example.grocerystoretest.view.BillListFragment

class BillViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            BillListFragment(mutableListOf(BillStatus.PAID))
        } else if (position == 1) {
            BillListFragment(mutableListOf(BillStatus.PREPARED))
        } else if (position == 2) {
            BillListFragment(mutableListOf(BillStatus.COMPLETED))
        } else {
            BillListFragment(mutableListOf(BillStatus.CANCELLED))
        }
    }
}