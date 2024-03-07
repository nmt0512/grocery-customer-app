package com.example.grocerystoretest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocerystoretest.R
import com.example.grocerystoretest.databinding.ItemHomeProductBinding
import com.example.grocerystoretest.model.response.product.ProductResponse

class RecyclerViewRecommendedProductAdapter(private val productResponseList: List<ProductResponse>) :
    RecyclerView.Adapter<RecyclerViewRecommendedProductAdapter.RecommendedProductViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendedProductViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_home_product, parent, false)
        val binding = ItemHomeProductBinding.bind(view)
        return RecommendedProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendedProductViewHolder, position: Int) {
        holder.bind(productResponseList[position])
    }

    override fun getItemCount(): Int {
        return productResponseList.size
    }

    inner class RecommendedProductViewHolder(val binding: ItemHomeProductBinding) :
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