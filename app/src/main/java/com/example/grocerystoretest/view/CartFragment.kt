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

    private lateinit var cartResponseList: MutableList<CartResponse>
    private val checkedCartItemSet = mutableSetOf<String>()
    private val checkedOutOfStockCartItemSet = mutableSetOf<String>()

    private var totalPrice = 0
    private lateinit var deleteConfirmDialog: AlertDialog
    private lateinit var unavailableProductDialog: AlertDialog

    override fun getContentLayout(): Int {
        return R.layout.fragment_cart
    }

    override fun initView() {

        loadingDialog?.show()

        cartViewModel = CartViewModel(this.requireContext())
        productViewModel = ProductViewModel(this.requireContext())

        val deleteConfirmDialogListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    val deletingCartIdList = mutableListOf<String>()
                    deletingCartIdList.addAll(checkedCartItemSet)
                    deletingCartIdList.addAll(checkedOutOfStockCartItemSet)
                    if (deletingCartIdList.isNotEmpty()) {
                        cartViewModel
                            .deleteCartByIdList(deletingCartIdList)
                            .observe(this) { response ->
                                response.deletedIdList.isNotEmpty().let {
                                    cartResponseList
                                        .filter { checkedCartItemSet.contains(it.id) }
                                        .forEach { minusTotalPrice(it.product.unitPrice * it.quantity) }
                                    cartResponseList.removeIf { deletingCartIdList.contains(it.id) }

                                    checkedCartItemSet.clear()
                                    checkedOutOfStockCartItemSet.clear()

                                    binding.rvCart.adapter = RecyclerViewCartAdapter(cartResponseList, this)
                                    dialog.dismiss()
                                    Toast.makeText(
                                        this.requireContext(),
                                        "Xóa sản phẩm thành công",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    totalPrice = 0
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
            binding.rvCart.adapter = RecyclerViewCartAdapter(cartResponseList, this)
            loadingDialog?.hide()
        }
    }

    override fun onResume() {
        totalPrice = 0
        binding.txtCartTotalPrice.text =
            "Tổng tiền: ${NumberConverterUtil.convertNumberToStringWithDot(totalPrice)} Đ"
        checkedCartItemSet.clear()
        checkedOutOfStockCartItemSet.clear()
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
            binding.rvCart.adapter?.notifyItemChanged(position, it)
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

    override fun addToCheckedOutOfStockCartItemSet(cartId: String) {
        checkedOutOfStockCartItemSet.add(cartId)
    }

    override fun removeFromCheckedOutOfStockCartItemSet(cartId: String) {
        checkedOutOfStockCartItemSet.remove(cartId)
    }

    override fun isCheckedOutOfStockCartItemSetContains(cartId: String): Boolean {
        return checkedOutOfStockCartItemSet.contains(cartId)
    }

}