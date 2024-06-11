package com.example.grocerystoretest.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocerystoretest.R
import com.example.grocerystoretest.databinding.ItemHomeProductBinding
import com.example.grocerystoretest.model.response.product.ProductResponse
import com.example.grocerystoretest.utils.NumberConverterUtil
import com.example.grocerystoretest.view.IHomeFragment
import com.example.grocerystoretest.view.ProductDetailActivity

class RecyclerViewHomeProductAdapter(
    private val homeFragment: IHomeFragment,
    private val productResponseList: List<ProductResponse>
) :
    RecyclerView.Adapter<RecyclerViewHomeProductAdapter.HomeProductViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeProductViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_home_product, parent, false)
        val binding = ItemHomeProductBinding.bind(view)
        return HomeProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productResponseList.size
    }

    override fun onBindViewHolder(holder: HomeProductViewHolder, position: Int) {
        holder.bind(productResponseList[position])
    }

    inner class HomeProductViewHolder(val binding: ItemHomeProductBinding) :
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
                homeFragment.showBottomSheetDialog(productResponse)
            }

            binding.imgProduct.setOnClickListener {
                startProductDetailActivity(productResponse.id)
            }
            binding.txtName.setOnClickListener {
                startProductDetailActivity(productResponse.id)
            }
            binding.txtPrice.setOnClickListener {
                startProductDetailActivity(productResponse.id)
            }
        }

        private fun startProductDetailActivity(productId: Int) {
            val intent = Intent(binding.root.context, ProductDetailActivity::class.java)
            intent.putExtra("productId", productId)
            binding.root.context.startActivity(intent)
        }
    }
}