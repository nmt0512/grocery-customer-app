package com.example.grocerystoretest.view

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
import com.example.grocerystoretest.model.request.bill.CreateBillRequest
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
import java.time.LocalTime

class OrderActivity : BaseActivity<ActivityOrderBinding>() {

    private lateinit var cartResponseList: List<CartResponse>

    private lateinit var dialogChoosePaymentMethodBinding: DialogChoosePaymentMethodBinding

    private lateinit var bottomSheetDialog: BottomSheetDialog

    private lateinit var billViewModel: BillViewModel
    private lateinit var paymentViewModel: PaymentViewModel

    private lateinit var paymentSheet: PaymentSheet

    private var choosedPaymentMethod = PaymentMethodEnum.NONE

    private var totalPrice = 0

    private var isBtnPaymentClicked = false

    private var pickUpDate = PickerObject.DATE_PICKER_LIST[0]
    private var pickUpTime = PickerObject.TIME_PICKER_LIST[0]

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

        binding.timePicker.minValue = 0
        binding.timePicker.maxValue = PickerObject.TIME_PICKER_LIST.size - 1
        binding.timePicker.wrapSelectorWheel = false
        binding.timePicker.displayedValues = PickerObject.TIME_PICKER_LIST.toTypedArray()

        binding.datePicker.minValue = 0
        binding.datePicker.maxValue = PickerObject.DATE_PICKER_LIST.size - 1
        binding.datePicker.wrapSelectorWheel = false
        binding.datePicker.displayedValues = PickerObject.DATE_PICKER_LIST.toTypedArray()

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initListener() {
        binding.btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
        binding.layoutPaymentMethod.setOnClickListener {
            bottomSheetDialog.show()
        }

        dialogChoosePaymentMethodBinding.layout.setOnClickListener {
            binding.imageViewChoosedPaymentMethod.setImageResource(R.drawable.stripe_payment_logo)
            val choosedPaymentMethodName = dialogChoosePaymentMethodBinding.txtPaymentMethod.text
            if (choosedPaymentMethodName.equals(PaymentMethodEnum.STRIPE.longName)) {
                choosedPaymentMethod = PaymentMethodEnum.STRIPE
            }
            binding.txtChoosedPaymentMethod.text = choosedPaymentMethodName
            binding.layoutChoosedPaymentMethod.visibility = View.VISIBLE
            bottomSheetDialog.dismiss()
        }

        binding.btnPayment.setOnClickListener {
            if (!isBtnPaymentClicked) {
                if (pickUpDate == PickerObject.DATE_PICKER_LIST[0] || pickUpTime == PickerObject.TIME_PICKER_LIST[0]) {
                    Toast.makeText(this, "Bạn chưa chọn thời gian lấy hàng", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val lastPickTimeValid = LocalTime.of(
                        PickerObject.TIME_PICKER_LIST.last().substringBefore(":").toInt(),
                        PickerObject.TIME_PICKER_LIST.last().substringAfter(":").toInt()
                    ).minusHours(1)
                    val isPickTimeInvalid = LocalTime.of(
                        pickUpTime.substringBefore(":").toInt(),
                        pickUpTime.substringAfter(":").toInt()
                    ).minusHours(1).isBefore(LocalTime.now()) || LocalTime.now()
                        .isAfter(lastPickTimeValid)
                    if (pickUpDate == PickerObject.DATE_PICKER_LIST[1] && isPickTimeInvalid) {
                        Toast.makeText(
                            this,
                            "Thời gian lấy hàng phải sau thời điểm hiện tại ít nhất 1h",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        if (choosedPaymentMethod == PaymentMethodEnum.NONE) {
                            Toast.makeText(
                                this,
                                "Bạn chưa chọn phương thức thanh toán",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else if (choosedPaymentMethod == PaymentMethodEnum.STRIPE) {
                            isBtnPaymentClicked = true
                            requestStripePayment()
                        }
                    }
                }
            }
        }

        binding.timePicker.setOnValueChangedListener { picker, oldVal, newVal ->
            pickUpTime = PickerObject.TIME_PICKER_LIST[newVal]
        }

        binding.datePicker.setOnValueChangedListener { picker, oldVal, newVal ->
            pickUpDate = PickerObject.DATE_PICKER_LIST[newVal]
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
                isBtnPaymentClicked = false
            }

            is PaymentSheetResult.Failed -> {
                Toast.makeText(this, "Có lỗi xảy ra trong quá trình thanh toán", Toast.LENGTH_SHORT)
                    .show()
                isBtnPaymentClicked = false
            }

            is PaymentSheetResult.Completed -> {
                val cartIdList = cartResponseList.map { it.id }
                billViewModel.createBill(
                    CreateBillRequest(
                        cartIdList,
                        pickUpDate,
                        pickUpTime
                    )
                ).observe(this) {
                    if (it != BillResponse()) {
                        Toast.makeText(this, "Thanh toán thành công", Toast.LENGTH_SHORT).show()
                        this.onBackPressedDispatcher.onBackPressed()
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


    object PickerObject {

        val TIME_PICKER_LIST = mutableListOf("Giờ")
        val DATE_PICKER_LIST = mutableListOf("Ngày", "Hôm nay", "Ngày mai", "Ngày kia")

        private const val startTime = "08:30"
        private const val endTime = "21:30"
        private const val intervalMinutes = 30

        init {
            var currentTime = startTime

            while (currentTime <= endTime) {
                TIME_PICKER_LIST.add(currentTime)
                val currentMinutes = currentTime.substringAfter(":").toInt()
                val nextMinutes = (currentMinutes + intervalMinutes) % 60
                val nextHours = (currentTime.substringBefore(":")
                    .toInt() + (currentMinutes + intervalMinutes) / 60) % 24
                currentTime = String.format("%02d:%02d", nextHours, nextMinutes)
            }
        }
    }

}