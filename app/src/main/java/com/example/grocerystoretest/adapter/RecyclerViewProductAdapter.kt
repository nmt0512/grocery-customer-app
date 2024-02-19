package com.example.grocerystoretest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocerystoretest.R
import com.example.grocerystoretest.databinding.ItemProductBinding
import com.example.grocerystoretest.model.response.product.ProductResponse

class RecyclerViewProductAdapter(
    private val context: Context,
    private val productResponseList: List<ProductResponse>
) :
    RecyclerView.Adapter<RecyclerViewProductAdapter.ProductViewHolder>() {
    private lateinit var itemProductBinding: ItemProductBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        itemProductBinding = ItemProductBinding.bind(view)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.itemView.apply {
            val productResponse = productResponseList[position]
            itemProductBinding.txtName.text = productResponse.name
            if (productResponse.images.isNotEmpty()) {
                Glide.with(context)
                    .load(productResponse.images[0])
                    .centerInside()
                    .into(itemProductBinding.imgProduct)
            }
            val priceStr = "Giá: ${productResponse.unitPrice}Đ"
            itemProductBinding.txtPrice.text = priceStr
        }

    }

    override fun getItemCount(): Int {
        return productResponseList.size
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}