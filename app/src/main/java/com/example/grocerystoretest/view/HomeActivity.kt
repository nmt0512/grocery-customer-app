package com.example.grocerystoretest.view

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.grocerystoretest.R
import com.example.grocerystoretest.base.BaseActivity
import com.example.grocerystoretest.databinding.ActivityHomeBinding
import com.example.grocerystoretest.model.request.customer_device.CreateCustomerDeviceRequest
import com.example.grocerystoretest.viewmodel.CustomerDeviceViewModel
import com.example.grocerystoretest.viewmodel.UserInfoViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    private lateinit var userInfoViewModel: UserInfoViewModel
    private lateinit var customerDeviceViewModel: CustomerDeviceViewModel

    private lateinit var homeFragment: HomeFragment
    private lateinit var billFragment: BillFragment
    private lateinit var cartFragment: CartFragment
    private lateinit var accountFragment: AccountFragment

    override fun getContentLayout(): Int {
        return R.layout.activity_home
    }

    override fun initView() {
        userInfoViewModel = UserInfoViewModel(this)
        userInfoViewModel.getUserInfo()

        customerDeviceViewModel = CustomerDeviceViewModel(this)
        initFirebase()

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

    private fun initFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("initFirebase", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                task.result?.let {
                    Log.d("initFirebase", it)
                    customerDeviceViewModel.createCustomerDevice(
                        CreateCustomerDeviceRequest(getDeviceId(this), it)
                    )
                }
            },
        )
    }

    @SuppressLint("HardwareIds")
    private fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID,
        )
    }
}