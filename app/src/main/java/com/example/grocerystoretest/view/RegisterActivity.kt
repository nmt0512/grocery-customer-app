package com.example.grocerystoretest.view

import android.widget.Toast
import com.example.grocerystoretest.R
import com.example.grocerystoretest.base.BaseActivity
import com.example.grocerystoretest.databinding.ActivityRegisterBinding
import com.example.grocerystoretest.model.request.auth.RegisterCustomerRequest
import com.example.grocerystoretest.viewmodel.UserInfoViewModel
import java.util.regex.Pattern

class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    private lateinit var userInfoViewModel: UserInfoViewModel
    private val emailRegex = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
    private val phoneRegex = "^[0-9]{10}$"

    override fun getContentLayout(): Int {
        return R.layout.activity_register
    }

    override fun initView() {
        userInfoViewModel = UserInfoViewModel(this)
    }

    override fun initListener() {
        binding.btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
        binding.btnRegister.setOnClickListener {
            if (isValidated()) {
                val registerCustomerRequest = RegisterCustomerRequest(
                    binding.txtPhoneNumber.text.trim().toString(),
                    binding.txtPassword.text.trim().toString(),
                    binding.txtFirstName.text.trim().toString(),
                    binding.txtLastName.text.trim().toString(),
                    binding.txtEmail.text.trim().toString()
                )
                userInfoViewModel.registerCustomer(registerCustomerRequest).observe(this) {
                    Toast.makeText(
                        this,
                        "Đăng ký tài khoản thành công",
                        Toast.LENGTH_SHORT
                    ).show()
                    this.onBackPressedDispatcher.onBackPressed()
                }
            }
        }
    }

    override fun observeData() {

    }

    private fun isValidated(): Boolean {
        if (binding.txtPhoneNumber.text.isNullOrBlank() || binding.txtFirstName.text.isNullOrBlank()
            || binding.txtFirstName.text.isNullOrBlank() || binding.txtLastName.text.isNullOrBlank()
            || binding.txtEmail.text.isNullOrBlank() || binding.txtPassword.text.isNullOrBlank()
            || binding.txtRetypePassword.text.isNullOrBlank()
        ) {
            Toast.makeText(
                this,
                "Bạn cần điền đầy đủ thông tin",
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else {
            if (binding.txtPassword.text.toString() != binding.txtRetypePassword.text.toString()
            ) {
                Toast.makeText(
                    this,
                    "Mật khẩu bạn nhập lại không trùng khớp",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.txtPassword.text.trim().length < 9 || binding.txtPassword.text.trim().length > 20) {
                Toast.makeText(
                    this,
                    "Mật khẩu phải từ 9 đến 20 ký tự",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (!Pattern.compile(emailRegex).matcher(binding.txtEmail.text).matches()) {
                Toast.makeText(
                    this,
                    "Bạn phải nhập đúng định dạng email",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (!binding.txtPhoneNumber.text.matches(phoneRegex.toRegex())) {
                Toast.makeText(
                    this,
                    "Bạn phải nhập đúng định dạng số điện thoại",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        }
        return true
    }
}