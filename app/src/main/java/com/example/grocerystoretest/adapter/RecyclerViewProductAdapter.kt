package com.example.grocerystoretest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocerystoretest.R
import com.example.grocerystoretest.databinding.ItemProductBinding
import com.example.grocerystoretest.model.response.product.ProductResponse
import com.example.grocerystoretest.utils.NumberConverterUtil
import com.example.grocerystoretest.view.IProductListActivity

class RecyclerViewProductAdapter(
    private val productListActivity: IProductListActivity,
    private val productResponseList: List<ProductResponse>
) :
    RecyclerView.Adapter<RecyclerViewProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        val itemProductBinding = ItemProductBinding.bind(view)
        return ProductViewHolder(itemProductBinding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productResponseList[position])
    }

    override fun getItemCount(): Int {
        return productResponseList.size
    }

    inner class ProductViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(productResponse: ProductResponse) {
            binding.txtName.text = productResponse.name
            if (productResponse.images.isNotEmpty()) {
                Glide.with(binding.root)
                    .load(productResponse.images[0])
                    .centerInside()
                    .into(binding.imgProduct)
            }
            val priceStr =
                NumberConverterUtil.getProductPriceStringByPrice(productResponse.unitPrice)
            binding.txtPrice.text = priceStr

            binding.btnAddToCart.setOnClickListener {
                productListActivity.showBottomSheetDialog(productResponse)
            }
        }
    }
}