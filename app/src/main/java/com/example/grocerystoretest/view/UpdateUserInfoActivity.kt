package com.example.grocerystoretest.view

import android.widget.Toast
import com.example.grocerystoretest.R
import com.example.grocerystoretest.base.BaseActivity
import com.example.grocerystoretest.databinding.ActivityUpdateUserInfoBinding
import com.example.grocerystoretest.model.User
import com.example.grocerystoretest.model.request.auth.UpdateUserInfoRequest
import com.example.grocerystoretest.model.response.auth.UserInfoResponse
import com.example.grocerystoretest.utils.ApplicationPreference
import com.example.grocerystoretest.viewmodel.UserInfoViewModel
import java.util.regex.Pattern

class UpdateUserInfoActivity : BaseActivity<ActivityUpdateUserInfoBinding>() {

    private lateinit var userInfoViewModel: UserInfoViewModel

    private val emailRegex = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"

    override fun getContentLayout(): Int {
        return R.layout.activity_update_user_info
    }

    override fun initView() {
        userInfoViewModel = UserInfoViewModel(this)
        val userInfo = ApplicationPreference.getInstance(this)?.getUserInfo()
        binding.txtPhoneNumber.setText(userInfo?.phoneNumber)
        binding.txtFirstName.setText(userInfo?.firstName)
        binding.txtLastName.setText(userInfo?.lastName)
        binding.txtEmail.setText(userInfo?.email)
    }

    override fun initListener() {
        binding.btnSaveInfo.setOnClickListener {
            if (isValidated()) {
                val updateUserInfoRequest = UpdateUserInfoRequest(
                    binding.txtFirstName.text.trim().toString(),
                    binding.txtLastName.text.trim().toString(),
                    binding.txtEmail.text.trim().toString()
                )
                userInfoViewModel.updateUserInfo(updateUserInfoRequest).observe(this) {
                    if (it != UserInfoResponse()) {
                        val user = User(
                            it.id,
                            it.phoneNumber,
                            it.email,
                            it.firstName,
                            it.lastName
                        )
                        ApplicationPreference.getInstance(this)?.saveUserInfo(user)
                        Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT)
                            .show()
                        this.onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        }
    }

    override fun observeData() {

    }

    private fun isValidated(): Boolean {
        val userInfo = ApplicationPreference.getInstance(this)?.getUserInfo()
        if (binding.txtFirstName.text.isNullOrBlank() || binding.txtFirstName.text.isNullOrBlank()
            || binding.txtLastName.text.isNullOrBlank() || binding.txtEmail.text.isNullOrBlank()
        ) {
            Toast.makeText(
                this,
                "Bạn cần điền đầy đủ thông tin",
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.txtFirstName.text.trim() == userInfo?.firstName
            && binding.txtLastName.text.trim() == userInfo.lastName
            && binding.txtEmail.text.trim() == userInfo.email
        ) {
            Toast.makeText(
                this,
                "Bạn chưa nhập thông tin cần thay đổi",
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
        }
        return true
    }
}