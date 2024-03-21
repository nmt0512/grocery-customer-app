package com.example.grocerystoretest.view

import android.content.Intent
import android.widget.Toast
import com.example.grocerystoretest.R
import com.example.grocerystoretest.base.BaseActivity
import com.example.grocerystoretest.databinding.ActivityLoginBinding
import com.example.grocerystoretest.viewmodel.LoginViewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private lateinit var loginViewModel: LoginViewModel

    override fun getContentLayout(): Int {
        return R.layout.activity_login
    }

    override fun initView() {
        loginViewModel = LoginViewModel(this)
    }

    override fun initListener() {
        binding.btnLogin.setOnClickListener {
            loadingDialog?.show()
            val phoneNumber = binding.txtPhoneNumber.text.toString()
            val password = binding.txtPassword.text.toString()
            loginViewModel.login(phoneNumber, password)
        }

        binding.txtSignUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun observeData() {
        loginViewModel.isSuccessfullyLoggedIn.observe(this) {
            if (it) {
                startActivity(Intent(this, HomeActivity::class.java))
                loadingDialog?.dismiss()
                finishAffinity()
            } else {
                Toast.makeText(
                    this,
                    "Tên đăng nhập hoặc mật khẩu không chính xác",
                    Toast.LENGTH_SHORT
                ).show()
            }
            loadingDialog?.dismiss()
        }
    }
}