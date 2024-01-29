package com.example.grocerystoretest.view

import androidx.fragment.app.Fragment
import com.example.grocerystoretest.R
import com.example.grocerystoretest.base.BaseActivity
import com.example.grocerystoretest.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var homeFragment: HomeFragment
    private lateinit var billFragment: BillFragment
    private lateinit var cartFragment: CartFragment
    private lateinit var accountFragment: AccountFragment

    override fun getContentLayout(): Int {
        return R.layout.activity_main
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

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
            commit()
        }
    }
}