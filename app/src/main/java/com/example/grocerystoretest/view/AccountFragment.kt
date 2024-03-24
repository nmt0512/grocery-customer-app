package com.example.grocerystoretest.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import com.example.grocerystoretest.R
import com.example.grocerystoretest.base.BaseFragment
import com.example.grocerystoretest.databinding.FragmentAccountBinding
import com.example.grocerystoretest.utils.ApplicationPreference

class AccountFragment : BaseFragment<FragmentAccountBinding>() {

    private lateinit var logoutConfirmDialog: AlertDialog

    override fun getContentLayout(): Int {
        return R.layout.fragment_account
    }

    override fun initView() {
        val userInfo = ApplicationPreference.getInstance(this.requireContext())?.getUserInfo()
        userInfo?.let {
            binding.txtFullname.text = "Xin chào, ${userInfo?.firstName} ${userInfo?.lastName}"
        }

        val logoutConfirmDialogListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    ApplicationPreference.getInstance(this.requireContext())?.logout()
                    val intent = Intent(this.requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    this.activity?.finishAffinity()
                }

                DialogInterface.BUTTON_NEGATIVE -> {
                    dialog.dismiss()
                }
            }
        }
        logoutConfirmDialog = AlertDialog.Builder(this.requireContext())
            .setTitle("Đăng xuất")
            .setMessage("Bạn có chắc chắn muốn đăng xuất ?")
            .setPositiveButton("OK", logoutConfirmDialogListener)
            .setNegativeButton("Cancel", logoutConfirmDialogListener)
            .create()
    }

    override fun onResume() {
        initView()
        super.onResume()
    }

    override fun initListener() {
        binding.layoutUserInfo.setOnClickListener {
            val intent = Intent(this.requireContext(), UpdateUserInfoActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            logoutConfirmDialog.show()
        }

        binding.layoutChangePassword.setOnClickListener {
            val intent = Intent(this.requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }
    }

    override fun observeLiveData() {

    }

}