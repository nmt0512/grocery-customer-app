package com.example.grocerystoretest.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.grocerystoretest.R

abstract class BaseFragment<BINDING : ViewDataBinding> :
    Fragment() {

    lateinit var binding: BINDING
    var loadingDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = setupProgressDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            getContentLayout(),
            container,
            false,
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        observeLiveData()
    }

    abstract fun getContentLayout(): Int

    abstract fun initView()

    abstract fun initListener()

    abstract fun observeLiveData()

    private fun setupProgressDialog(): AlertDialog? {
        if (context != null) {
            val builder: AlertDialog.Builder =
                AlertDialog.Builder(requireContext(), R.style.CustomDialog)
            builder.setCancelable(false)

            val myLayout = LayoutInflater.from(requireContext())
            val dialogView: View = myLayout.inflate(R.layout.fragment_progress_dialog, null)

            builder.setView(dialogView)

            val dialog: AlertDialog = builder.create()
            val window: Window? = dialog.window
            if (window != null) {
                val layoutParams = WindowManager.LayoutParams()
                layoutParams.copyFrom(dialog.window?.attributes)
                layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
                dialog.window?.attributes = layoutParams
            }
            return dialog
        }
        return null
    }
}
