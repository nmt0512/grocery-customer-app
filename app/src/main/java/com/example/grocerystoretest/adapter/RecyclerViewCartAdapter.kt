package com.example.grocerystoretest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocerystoretest.R
import com.example.grocerystoretest.databinding.ItemCartBinding
import com.example.grocerystoretest.model.request.cart.UpdateCartQuantityRequest
import com.example.grocerystoretest.model.response.cart.CartResponse
import com.example.grocerystoretest.utils.NumberConverterUtil
import com.example.grocerystoretest.view.ICartFragment

class RecyclerViewCartAdapter(
    private val cartResponseList: List<CartResponse>,
    private val cartFragment: ICartFragment
) : RecyclerView.Adapter<RecyclerViewCartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        val binding = ItemCartBinding.bind(view)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cartResponseList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartResponseList[position], position)
    }

    inner class CartViewHolder(val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartResponse: CartResponse, position: Int) {
            val productResponse = cartResponse.product
            if (position == 0) {
                binding.root.background = null
            }
            if (productResponse.images.isNotEmpty()) {
                Glide.with(binding.root)
                    .load(productResponse.images[0])
                    .centerInside()
                    .into(binding.imageViewCartProduct)
            }
            binding.txtProductName.text = productResponse.name
            binding.txtProductPrice.text =
                NumberConverterUtil.getProductPriceStringByPrice(productResponse.unitPrice)
            binding.txtQuantity.text = cartResponse.quantity.toString()

            if (productResponse.quantity == 0) {
                binding.root.background = null
                binding.root.alpha = 0.5F
                binding.root.isEnabled = false
                binding.checkboxCartItem.visibility = View.GONE
                binding.txtOutOfStock.visibility = View.VISIBLE
            } else {
                binding.checkboxCartItem.isChecked =
                    cartFragment.isCheckedCartItemSetContains(cartResponse.id)

                binding.btnMinusQuantity.setOnClickListener {
                    binding.root.isClickable = false
                    val oldQuantity = binding.txtQuantity.text.toString().toInt()
                    if (oldQuantity > 1) {
                        val newQuantity = oldQuantity - 1
                        if (binding.checkboxCartItem.isChecked) {
                            cartFragment.updateCartQuantity(
                                position,
                                UpdateCartQuantityRequest(cartResponse.id, newQuantity),
                                false,
                                productResponse.unitPrice
                            )
                        } else {
                            cartFragment.updateCartQuantity(
                                position,
                                UpdateCartQuantityRequest(cartResponse.id, newQuantity),
                                null,
                                null
                            )
                        }
                    }
                }

                binding.btnPlusQuantity.setOnClickListener {
                    binding.root.isClickable = false
                    val newQuantity = binding.txtQuantity.text.toString().toInt() + 1
                    if (binding.checkboxCartItem.isChecked) {
                        cartFragment.updateCartQuantity(
                            position,
                            UpdateCartQuantityRequest(cartResponse.id, newQuantity),
                            true,
                            productResponse.unitPrice
                        )
                    } else {
                        cartFragment.updateCartQuantity(
                            position,
                            UpdateCartQuantityRequest(cartResponse.id, newQuantity),
                            null,
                            null
                        )
                    }
                }

                binding.checkboxCartItem.setOnClickListener {
                    val updatingMoney =
                        binding.txtQuantity.text.toString().toInt().times(productResponse.unitPrice)
                    if (binding.checkboxCartItem.isChecked) {
                        cartFragment.addToCheckedCartItemSet(cartResponse.id)
                        cartFragment.plusTotalPrice(updatingMoney)
                    } else {
                        cartFragment.removeFromCheckedCartItemSet(cartResponse.id)
                        cartFragment.minusTotalPrice(updatingMoney)
                    }
                }
            }

        }
    }
}
