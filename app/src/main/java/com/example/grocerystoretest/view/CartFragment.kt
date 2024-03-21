package com.example.grocerystoretest.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerystoretest.R
import com.example.grocerystoretest.adapter.RecyclerViewCartAdapter
import com.example.grocerystoretest.base.BaseFragment
import com.example.grocerystoretest.databinding.FragmentCartBinding
import com.example.grocerystoretest.model.request.cart.UpdateCartQuantityRequest
import com.example.grocerystoretest.model.request.product.AvailableProductRequest
import com.example.grocerystoretest.model.request.product.GetAvailableProductListRequest
import com.example.grocerystoretest.model.response.cart.CartResponse
import com.example.grocerystoretest.utils.NumberConverterUtil
import com.example.grocerystoretest.viewmodel.CartViewModel
import com.example.grocerystoretest.viewmodel.ProductViewModel
import com.google.gson.Gson

class CartFragment : BaseFragment<FragmentCartBinding>(),
    ICartFragment {

    private lateinit var cartViewModel: CartViewModel
    private lateinit var productViewModel: ProductViewModel
    private lateinit var recyclerViewCartAdapter: RecyclerViewCartAdapter

    private lateinit var cartResponseList: MutableList<CartResponse>
    private val checkedCartItemSet = mutableSetOf<String>()

    private var totalPrice = 0
    private lateinit var deleteConfirmDialog: AlertDialog
    private lateinit var unavailableProductDialog: AlertDialog

//    private val merchantName = "CGV Cinemas"
//    private val merchantCode = "CGV19072017"
//    private val description = "Thanh toán hóa đơn mua hàng"

    override fun getContentLayout(): Int {
        return R.layout.fragment_cart
    }

    override fun initView() {

//        AppMoMoLib.getInstance()
//            .setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT); // AppMoMoLib.ENVIRONMENT.PRODUCTION

        loadingDialog?.show()

        cartViewModel = CartViewModel(this.requireContext())
        productViewModel = ProductViewModel(this.requireContext())

        val deleteConfirmDialogListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    if (checkedCartItemSet.isNotEmpty()) {
                        cartViewModel
                            .deleteCartByIdList(checkedCartItemSet.toMutableList())
                            .observe(this) { response ->
                                response.deletedIdList.isNotEmpty().let {
                                    cartResponseList
                                        .filter { checkedCartItemSet.contains(it.id) }
                                        .forEach { minusTotalPrice(it.product.unitPrice * it.quantity) }
                                    cartResponseList.removeIf { checkedCartItemSet.contains(it.id) }
                                    checkedCartItemSet.clear()
                                    recyclerViewCartAdapter.notifyDataSetChanged()
                                    dialog.dismiss()
                                    Toast.makeText(
                                        this.requireContext(),
                                        "Xóa sản phẩm thành công",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        dialog.dismiss()
                        Toast.makeText(
                            this.requireContext(),
                            "Bạn chưa chọn sản phẩm nào để xóa",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

                DialogInterface.BUTTON_NEGATIVE -> {
                    dialog.dismiss()
                }
            }
        }
        deleteConfirmDialog = AlertDialog.Builder(this.requireContext())
            .setTitle("Xóa sản phẩm")
            .setMessage("Bạn có chắc chắn muốn xóa các sản phẩm đã chọn ?")
            .setPositiveButton("Yes", deleteConfirmDialogListener)
            .setNegativeButton("No", deleteConfirmDialogListener)
            .create()

        val unavailableProductDialogListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    dialog.dismiss()
                }
            }
        }
        unavailableProductDialog = AlertDialog.Builder(this.requireContext())
            .setTitle("Sản phẩm không đủ số lượng")
            .setCancelable(true)
            .setPositiveButton("OK", unavailableProductDialogListener)
            .create()

        binding.txtCartTotalPrice.text =
            "Tổng tiền: ${NumberConverterUtil.convertNumberToStringWithDot(totalPrice)} Đ"
        binding.rvCart.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun initListener() {
        binding.imageViewDeleteCartItem.setOnClickListener {
            deleteConfirmDialog.show()
        }
        binding.btnOrder.setOnClickListener {
            if (checkedCartItemSet.isNotEmpty()) {
                val checkCartResponseList = cartResponseList
                    .filter { checkedCartItemSet.contains(it.id) }
                val availableProductRequestList = checkCartResponseList
                    .map { AvailableProductRequest(it.product.id, it.quantity) }
                val getAvailableProductListResponseLiveData =
                    productViewModel.getAvailableProductList(
                        GetAvailableProductListRequest(availableProductRequestList)
                    )
                getAvailableProductListResponseLiveData.observe(this) {
                    if (it.isAvailable) {
                        val intent = Intent(this.requireContext(), OrderActivity::class.java)
                        intent.putExtra("cartResponseList", Gson().toJson(checkCartResponseList))
                        intent.putExtra("totalPrice", totalPrice)
                        startActivity(intent)
                    } else {
                        val stringBuilder = StringBuilder("Các sản phẩm không đủ số lượng: ")
                        it.unavailableProductResponseList?.forEach { productResponse ->
                            stringBuilder.append("\n - ${productResponse.name}")
                        }
                        unavailableProductDialog.setMessage(stringBuilder.toString())
                        unavailableProductDialog.show()
                    }
                }
            } else {
                Toast.makeText(
                    this.requireContext(),
                    "Bạn chưa chọn sản phẩm nào",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    override fun observeLiveData() {
        cartViewModel.getAllItemInCart()
        cartViewModel.cartResponseListLiveData.observe(this) {
            cartResponseList = it.toMutableList()
            recyclerViewCartAdapter = RecyclerViewCartAdapter(cartResponseList, this)
            binding.rvCart.adapter = recyclerViewCartAdapter
            loadingDialog?.hide()
        }
    }

    override fun onResume() {
        totalPrice = 0
        binding.txtCartTotalPrice.text =
            "Tổng tiền: ${NumberConverterUtil.convertNumberToStringWithDot(totalPrice)} Đ"
        checkedCartItemSet.clear()
        observeLiveData()
        super.onResume()
    }

    override fun addToCheckedCartItemSet(cartId: String) {
        checkedCartItemSet.add(cartId)
    }

    override fun removeFromCheckedCartItemSet(cartId: String) {
        checkedCartItemSet.remove(cartId)
    }

    override fun isCheckedCartItemSetContains(cartId: String): Boolean {
        return checkedCartItemSet.contains(cartId)
    }

    override fun plusTotalPrice(inputMoney: Int) {
        Log.i("PLUS MONEY", inputMoney.toString())
        totalPrice += inputMoney
        binding.txtCartTotalPrice.text =
            "Tổng tiền: ${NumberConverterUtil.convertNumberToStringWithDot(totalPrice)} Đ"
    }

    override fun minusTotalPrice(inputMoney: Int) {
        Log.i("MINUS MONEY", inputMoney.toString())
        totalPrice -= inputMoney
        binding.txtCartTotalPrice.text =
            "Tổng tiền: ${NumberConverterUtil.convertNumberToStringWithDot(totalPrice)} Đ"
    }

    override fun updateCartQuantity(
        position: Int,
        updateCartQuantityRequest: UpdateCartQuantityRequest,
        isPlusTotalPrice: Boolean?,
        updatingMoney: Int?
    ) {
        loadingDialog?.show()
        cartViewModel.updateCartQuantity(updateCartQuantityRequest).observe(this) {
            cartResponseList[position].quantity = it.quantity
            recyclerViewCartAdapter.notifyItemChanged(position, it)
            isPlusTotalPrice?.let {
                if (isPlusTotalPrice) {
                    plusTotalPrice(updatingMoney!!)
                } else {
                    minusTotalPrice(updatingMoney!!)
                }
            }
            loadingDialog?.hide()
        }
    }

//    private fun requestMomoPayment() {
//        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT)
//        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN)
//
//        val eventValue: MutableMap<String, Any> = HashMap()
//        // client Required
//        eventValue["merchantname"] =
//            merchantName // Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
//        eventValue["merchantcode"] =
//            merchantCode // Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
//        eventValue["amount"] = totalPrice // Kiểu integer
//        eventValue["orderId"] =
//            "orderId123456789" // uniqueue id cho BillId, giá trị duy nhất cho mỗi BILL
//        eventValue["orderLabel"] = "Mã đơn hàng" // gán nhãn
//
//        // client Optional - bill info
//        eventValue["merchantnamelabel"] = "Dịch vụ" // gán nhãn
//        eventValue["fee"] = 0 // Kiểu integer
//        eventValue["description"] = description // mô tả đơn hàng - short description
//
//        // client extra data
//        eventValue["requestId"] = merchantCode + "merchant_billId_" + System.currentTimeMillis()
//        eventValue["partnerCode"] = merchantCode
//        // Example extra data
//        val objExtraData = JSONObject()
//        try {
//            objExtraData.put("site_code", "008")
//            objExtraData.put("site_name", "CGV Cresent Mall")
//            objExtraData.put("screen_code", 0)
//            objExtraData.put("screen_name", "Special")
//            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3")
//            objExtraData.put("movie_format", "2D")
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//        eventValue["extraData"] = objExtraData.toString()
//        eventValue["extra"] = ""
//        AppMoMoLib.getInstance().requestMoMoCallBack(this.activity, eventValue)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode === AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode === -1) {
//            if (data != null) {
//                Log.e(">>>>>>>>>>", data.getIntExtra("status", -1).toString())
//                if (data.getIntExtra("status", -1) === 0) {
//                    Toast.makeText(
//                        this.requireContext(),
//                        "message: " + "Get token " + data.getStringExtra("message"),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    val token = data.getStringExtra("data") // Token response
//                    val phoneNumber = data.getStringExtra("phonenumber")
//
//                    var env: String? = data.getStringExtra("env")
//                    if (env == null) {
//                        env = "app"
//                    }
//                    if (token != null && token != "") {
//                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
//                        // IF Momo topup success, continue to process your order
//                    } else {
//                        //      this.toast("message: " + this.getString(R.string.not_receive_info))
//                    }
//                } else if (data.getIntExtra("status", -1) === 1) {
//                    // TOKEN FAIL
//                    val message =
//                        if (data.getStringExtra("message") != null) data.getStringExtra("message") else "Thất bại"
//                    //  this.toast("message: $message")
//                } else if (data.getIntExtra("status", -1) === 2) {
//                    // TOKEN FAIL
//                    //   this.toast("message: " + this.getString(R.string.not_receive_info))
//                } else {
//                    // TOKEN FAIL
//                    //  this.toast("message: " + this.getString(R.string.not_receive_info))
//                }
//            } else {
//                //   this.toast("message: " + this.getString(R.string.not_receive_info))
//            }
//        } else {
//            // this.toast("message: " + this.getString(R.string.not_receive_info_err))
//        }
//    }

}