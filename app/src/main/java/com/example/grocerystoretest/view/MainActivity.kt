package com.example.grocerystoretest.view

import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.example.grocerystoretest.R
import com.example.grocerystoretest.base.BaseActivity
import com.example.grocerystoretest.databinding.ActivityMainBinding
import com.example.grocerystoretest.model.request.auth.LoginRequest
import com.example.grocerystoretest.utils.ApplicationPreference
import com.example.grocerystoretest.viewmodel.LoginViewModel

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var loginViewModel: LoginViewModel

    override fun getContentLayout(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        loginViewModel = LoginViewModel(this)
        val savedLoginRequest = ApplicationPreference.getInstance(this)?.getLoginRequest()
        if (savedLoginRequest == LoginRequest()) {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            }, 1500)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                loginViewModel.login(savedLoginRequest?.phoneNumber!!, savedLoginRequest.password!!)
                    .observe(this) {
                        if (it) {
                            startActivity(Intent(this, HomeActivity::class.java))
                        } else {
                            startActivity(Intent(this, LoginActivity::class.java))
                        }
                        finishAffinity()
                    }
            }, 500)
        }
    }

    override fun initListener() {

    }

    override fun observeData() {

    }
}