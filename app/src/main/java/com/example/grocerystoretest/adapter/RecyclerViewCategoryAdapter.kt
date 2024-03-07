package com.example.grocerystoretest.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocerystoretest.R
import com.example.grocerystoretest.databinding.ItemCategoryBinding
import com.example.grocerystoretest.model.response.category.CategoryResponse
import com.example.grocerystoretest.view.ProductListActivity

class RecyclerViewCategoryAdapter(private val categoryList: List<CategoryResponse>) :
    RecyclerView.Adapter<RecyclerViewCategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        val binding = ItemCategoryBinding.bind(view)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categoryList[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.binding.root.context, ProductListActivity::class.java)
            intent.putExtra("categoryId", categoryList[position].id)
            holder.binding.root.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(categoryResponse: CategoryResponse) {
            binding.txtName.text = categoryResponse.name
            Glide.with(binding.root)
                .load(categoryResponse.imageUrl)
                .dontAnimate()
                .into(binding.categoryImageView)
            binding.categoryImageView.setBackgroundResource(R.drawable.bg_item_category_image)
        }
    }
}