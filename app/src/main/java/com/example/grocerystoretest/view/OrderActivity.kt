package com.example.grocerystoretest.view

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerystoretest.R
import com.example.grocerystoretest.adapter.RecyclerViewOrderListAdapter
import com.example.grocerystoretest.base.BaseActivity
import com.example.grocerystoretest.databinding.ActivityOrderBinding
import com.example.grocerystoretest.databinding.DialogChoosePaymentMethodBinding
import com.example.grocerystoretest.enums.PaymentMethodEnum
import com.example.grocerystoretest.model.response.bill.BillResponse
import com.example.grocerystoretest.model.response.cart.CartResponse
import com.example.grocerystoretest.utils.NumberConverterUtil
import com.example.grocerystoretest.viewmodel.BillViewModel
import com.example.grocerystoretest.viewmodel.PaymentViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult

class OrderActivity : BaseActivity<ActivityOrderBinding>() {

    private lateinit var cartResponseList: List<CartResponse>

    private lateinit var dialogChoosePaymentMethodBinding: DialogChoosePaymentMethodBinding

    private lateinit var bottomSheetDialog: BottomSheetDialog

    private lateinit var billViewModel: BillViewModel
    private lateinit var paymentViewModel: PaymentViewModel

    private lateinit var paymentSheet: PaymentSheet

    private lateinit var choosedPaymentMethod: PaymentMethodEnum

    private var totalPrice = 0

    override fun getContentLayout(): Int {
        return R.layout.activity_order
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun initView() {
        initButtonSheetDialog()

        billViewModel = BillViewModel(this)

        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        paymentViewModel = PaymentViewModel(this)

        loadingDialog?.show()
        val cartResponseListString = intent.getStringExtra("cartResponseList")
        val type = object : TypeToken<List<CartResponse>>() {}.type
        cartResponseList = Gson().fromJson(cartResponseListString, type)
        binding.rvOrderList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvOrderList.adapter = RecyclerViewOrderListAdapter(cartResponseList)

        totalPrice = intent.getIntExtra("totalPrice", 0)
        binding.txtTotalPrice.text =
            "Tổng tiền: ${NumberConverterUtil.convertNumberToStringWithDot(totalPrice)} Đ"
        loadingDialog?.hide()
    }

    override fun initListener() {
        binding.layoutPaymentMethod.setOnClickListener {
            bottomSheetDialog.show()
        }

        dialogChoosePaymentMethodBinding.layout.setOnClickListener {
            binding.imageViewChoosedPaymentMethod.setImageResource(R.drawable.stripe_payment_logo)
            val choosedPaymentMethodName = dialogChoosePaymentMethodBinding.txtPaymentMethod.text
            binding.txtChoosedPaymentMethod.text = choosedPaymentMethodName
            binding.layoutChoosedPaymentMethod.visibility = View.VISIBLE
            if (choosedPaymentMethodName.equals(PaymentMethodEnum.STRIPE.longName)) {
                choosedPaymentMethod = PaymentMethodEnum.STRIPE
            }
            bottomSheetDialog.hide()
        }

        binding.btnPayment.setOnClickListener {
            if (choosedPaymentMethod == PaymentMethodEnum.STRIPE) {
                requestStripePayment()
            }
        }
    }

    override fun observeData() {

    }

    private fun initButtonSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetDialogView = LayoutInflater
            .from(this)
            .inflate(R.layout.dialog_choose_payment_method, null)
        dialogChoosePaymentMethodBinding =
            DialogChoosePaymentMethodBinding.bind(bottomSheetDialogView)
        bottomSheetDialog.setContentView(bottomSheetDialogView)
    }

    private fun requestStripePayment() {
        paymentViewModel.confirmPaymentStripe(totalPrice)
        paymentViewModel.confirmPaymentResponseLiveData.observe(this) {
            val paymentIntentClientSecret = it.paymentIntent
            val customerConfig = PaymentSheet.CustomerConfiguration(
                it.customer,
                it.ephemeralKey
            )
            val publishableKey = it.publishableKey
            PaymentConfiguration.init(this, publishableKey)
            paymentSheet.presentWithPaymentIntent(
                paymentIntentClientSecret,
                PaymentSheet.Configuration(
                    merchantDisplayName = "GROCERY STORE",
                    customer = customerConfig,
                    // Set `allowsDelayedPaymentMethods` to true if your business handles
                    // delayed notification payment methods like US bank accounts.
                    allowsDelayedPaymentMethods = true
                )
            )
        }
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        loadingDialog?.dismiss()
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                Toast.makeText(this, "Đã hủy thanh toán", Toast.LENGTH_SHORT).show()
            }

            is PaymentSheetResult.Failed -> {
                Toast.makeText(this, "Có lỗi xảy ra trong quá trình thanh toán", Toast.LENGTH_SHORT)
                    .show()
            }

            is PaymentSheetResult.Completed -> {
                val cartIdList = cartResponseList.map { it.id }
                billViewModel.createBill(cartIdList).observe(this) {
                    if (it != BillResponse()) {
                        Toast.makeText(this, "Thanh toán thành công", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this,
                            "Có lỗi xảy ra trong quá trình thanh toán",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
    }

}