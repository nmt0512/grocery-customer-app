package com.example.grocerystoretest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocerystoretest.R
import com.example.grocerystoretest.databinding.ItemHomeProductBinding
import com.example.grocerystoretest.model.response.product.ProductResponse

class RecyclerViewBestSellingProductAdapter(private val productResponseList: List<ProductResponse>) :
    RecyclerView.Adapter<RecyclerViewBestSellingProductAdapter.BestSellingProductViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BestSellingProductViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_home_product, parent, false)
        val binding = ItemHomeProductBinding.bind(view)
        return BestSellingProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productResponseList.size
    }

    override fun onBindViewHolder(holder: BestSellingProductViewHolder, position: Int) {
        holder.bind(productResponseList[position])
    }

    inner class BestSellingProductViewHolder(val binding: ItemHomeProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(productResponse: ProductResponse) {
            binding.txtName.text = productResponse.name
            if (productResponse.images.isNotEmpty()) {
                Glide.with(binding.root)
                    .load(productResponse.images[0])
                    .centerInside()
                    .into(binding.imgProduct)
            }
            val priceStr = "Giá: ${productResponse.unitPrice}Đ"
            binding.txtPrice.text = priceStr
        }
    }
}