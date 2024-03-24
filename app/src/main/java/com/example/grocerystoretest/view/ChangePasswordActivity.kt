package com.example.grocerystoretest.view

import android.widget.Toast
import com.example.grocerystoretest.R
import com.example.grocerystoretest.base.BaseActivity
import com.example.grocerystoretest.databinding.ActivityChangePasswordBinding
import com.example.grocerystoretest.model.request.auth.ChangePasswordRequest
import com.example.grocerystoretest.viewmodel.UserInfoViewModel

class ChangePasswordActivity : BaseActivity<ActivityChangePasswordBinding>() {

    private lateinit var userInfoViewModel: UserInfoViewModel

    override fun getContentLayout(): Int {
        return R.layout.activity_change_password
    }

    override fun initView() {
        userInfoViewModel = UserInfoViewModel(this)
    }

    override fun initListener() {
        binding.btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
        binding.btnConfirm.setOnClickListener {
            if (isValidated()) {
                userInfoViewModel.changePassword(
                    ChangePasswordRequest(
                        binding.txtOldPassword.text.trim().toString(),
                        binding.txtNewPassword.text.trim().toString()
                    )
                ).observe(this) {
                    if (it) {
                        Toast.makeText(
                            this,
                            "Đổi mật khẩu thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                        this.onBackPressedDispatcher.onBackPressed()
                    } else {
                        Toast.makeText(
                            this,
                            "Có lỗi xảy ra khi đổi mật khẩu",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun observeData() {

    }

    private fun isValidated(): Boolean {
        if (binding.txtOldPassword.text.isNullOrBlank() || binding.txtNewPassword.text.isNullOrBlank()
            || binding.txtRetypeNewPassword.text.isNullOrBlank()
        ) {
            Toast.makeText(
                this,
                "Bạn cần điền đầy đủ thông tin",
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else {
            if (binding.txtNewPassword.text.toString() != binding.txtRetypeNewPassword.text.toString()
            ) {
                Toast.makeText(
                    this,
                    "Mật khẩu bạn nhập lại không trùng khớp",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.txtNewPassword.text.trim().length < 9 || binding.txtNewPassword.text.trim().length > 20) {
                Toast.makeText(
                    this,
                    "Mật khẩu phải từ 9 đến 20 ký tự",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        }
        return true
    }
}