package com.example.grocerystoretest.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerystoretest.R
import com.example.grocerystoretest.adapter.RecyclerViewCartAdapter
import com.example.grocerystoretest.base.BaseFragment
import com.example.grocerystoretest.databinding.FragmentCartBinding
import com.example.grocerystoretest.model.request.cart.UpdateCartQuantityRequest
import com.example.grocerystoretest.model.response.cart.CartResponse
import com.example.grocerystoretest.utils.NumberConverterUtil
import com.example.grocerystoretest.viewmodel.CartViewModel

class CartFragment : BaseFragment<FragmentCartBinding>(),
    ICartFragment {

    private lateinit var cartViewModel: CartViewModel
    private lateinit var recyclerViewCartAdapter: RecyclerViewCartAdapter

    private lateinit var cartResponseList: MutableList<CartResponse>
    private val checkedCartItemSet = mutableSetOf<String>()

    private var totalPrice = 0
    private lateinit var deleteConfirmDialog: AlertDialog

    override fun getContentLayout(): Int {
        return R.layout.fragment_cart
    }

    override fun initView() {
        cartViewModel = CartViewModel(this.requireContext())

        val dialogListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    if (checkedCartItemSet.isNotEmpty()) {
                        cartViewModel
                            .deleteCartByIdList(checkedCartItemSet.toMutableList())
                            .observe(this) {
                                it.deletedIdList.isNotEmpty().let {
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
            .setPositiveButton("Yes", dialogListener)
            .setNegativeButton("No", dialogListener)
            .create()

        binding.txtCartTotalPrice.text = "Tổng tiền: ${NumberConverterUtil.convertNumberToStringWithDot(totalPrice)} Đ"
        binding.rvCart.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun initListener() {
        binding.imageViewDeleteCartItem.setOnClickListener {
            deleteConfirmDialog.show()
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

    override fun addToCheckedCartItemSet(cartId: String) {
        checkedCartItemSet.add(cartId)
//        Log.i("CHECKED CART ITEM", Gson().toJson(checkedCartItemList))
    }

    override fun removeFromCheckedCartItemSet(cartId: String) {
        checkedCartItemSet.remove(cartId)
//        Log.i("CHECKED CART ITEM", Gson().toJson(checkedCartItemList))
    }

    override fun isCheckedCartItemSetContains(cartId: String): Boolean {
        return checkedCartItemSet.contains(cartId)
    }

    override fun plusTotalPrice(inputMoney: Int) {
        Log.i("PLUS MONEY", inputMoney.toString())
        totalPrice += inputMoney
        binding.txtCartTotalPrice.text = "Tổng tiền: ${NumberConverterUtil.convertNumberToStringWithDot(totalPrice)}Đ"
    }

    override fun minusTotalPrice(inputMoney: Int) {
        Log.i("MINUS MONEY", inputMoney.toString())
        totalPrice -= inputMoney
        binding.txtCartTotalPrice.text = "Tổng tiền: ${NumberConverterUtil.convertNumberToStringWithDot(totalPrice)}Đ"
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

}