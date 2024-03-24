package com.example.grocerystoretest.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocerystoretest.R
import com.example.grocerystoretest.databinding.ItemSearchProductBinding
import com.example.grocerystoretest.model.response.product.ProductResponse
import com.example.grocerystoretest.utils.NumberConverterUtil
import com.example.grocerystoretest.view.ProductDetailActivity

class RecyclerViewSearchProductAdapter(private val productResponseList: List<ProductResponse>) :
    RecyclerView.Adapter<RecyclerViewSearchProductAdapter.SearchProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchProductViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_search_product, parent, false)
        val binding = ItemSearchProductBinding.bind(view)
        return SearchProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productResponseList.size
    }

    override fun onBindViewHolder(holder: SearchProductViewHolder, position: Int) {
        holder.bind(productResponseList[position])
    }

    inner class SearchProductViewHolder(val binding: ItemSearchProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(productResponse: ProductResponse) {
            binding.txtProductName.text = productResponse.name
            if (productResponse.images.isNotEmpty()) {
                Glide.with(binding.root)
                    .load(productResponse.images[0])
                    .centerInside()
                    .into(binding.imageViewProduct)
            }
            val priceStr =
                NumberConverterUtil.getProductPriceStringByPrice(productResponse.unitPrice)
            binding.txtProductPrice.text = priceStr

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, ProductDetailActivity::class.java)
                intent.putExtra("productId", productResponse.id)
                binding.root.context.startActivity(intent)
            }
        }
    }
}