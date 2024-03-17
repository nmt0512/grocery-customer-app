package com.example.grocerystoretest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocerystoretest.R
import com.example.grocerystoretest.databinding.ItemOrderBinding
import com.example.grocerystoretest.model.response.cart.CartResponse
import com.example.grocerystoretest.utils.NumberConverterUtil

class RecyclerViewOrderListAdapter(private val cartResponseList: List<CartResponse>) :
    RecyclerView.Adapter<RecyclerViewOrderListAdapter.OrderListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        val binding = ItemOrderBinding.bind(view)
        return OrderListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cartResponseList.size
    }

    override fun onBindViewHolder(holder: OrderListViewHolder, position: Int) {
        holder.bind(cartResponseList[position], position)
    }

    inner class OrderListViewHolder(val binding: ItemOrderBinding) :
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
                    .into(binding.imageViewProduct)
            }
            binding.txtProductName.text = productResponse.name
            binding.txtProductPrice.text =
                NumberConverterUtil.getProductPriceStringByPrice(productResponse.unitPrice)
            binding.txtCartQuantity.text = "x${cartResponse.quantity}"
        }
    }
}