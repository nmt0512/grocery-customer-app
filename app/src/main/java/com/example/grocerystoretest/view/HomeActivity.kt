package com.example.grocerystoretest.view

import androidx.fragment.app.Fragment
import com.example.grocerystoretest.R
import com.example.grocerystoretest.base.BaseActivity
import com.example.grocerystoretest.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    private lateinit var homeFragment: HomeFragment
    private lateinit var billFragment: BillFragment
    private lateinit var cartFragment: CartFragment
    private lateinit var accountFragment: AccountFragment

    override fun getContentLayout(): Int {
        return R.layout.activity_home
    }

    override fun initView() {
        homeFragment = HomeFragment()
        billFragment = BillFragment()
        cartFragment = CartFragment()
        accountFragment = AccountFragment()

        binding.bottomNavigationBar.selectedItemId = R.id.item_home
        loadFragment(homeFragment)
    }

    override fun initListener() {
        binding.bottomNavigationBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_home -> {
                    loadFragment(homeFragment)
                    true
                }

                R.id.item_cart -> {
                    loadFragment(cartFragment)
                    true
                }

                R.id.item_bill -> {
                    loadFragment(billFragment)
                    true
                }

                R.id.item_account -> {
                    loadFragment(accountFragment)
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    override fun observeData() {

    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
            commit()
        }
    }
}