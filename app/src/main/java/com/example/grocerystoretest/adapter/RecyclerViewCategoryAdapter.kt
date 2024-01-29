package com.example.grocerystoretest.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocerystoretest.R
import com.example.grocerystoretest.databinding.ItemCategoryBinding
import com.example.grocerystoretest.model.Category
import com.example.grocerystoretest.view.ProductListActivity

class RecyclerViewCategoryAdapter(
    private val context: Context,
    private val categoryList: List<Category>
) :
    RecyclerView.Adapter<RecyclerViewCategoryAdapter.CategoryViewHolder>() {
    private lateinit var itemCategoryBinding: ItemCategoryBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        itemCategoryBinding = ItemCategoryBinding.bind(view)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.itemView.apply {
            val category = categoryList[position]
            itemCategoryBinding.txtName.text = category.name
            Glide.with(context)
                .load(category.imageUrl)
                .dontAnimate()
                .into(itemCategoryBinding.categoryImageView)
            itemCategoryBinding.categoryImageView.setBackgroundResource(R.drawable.bg_item_category_image)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductListActivity::class.java)
            intent.putExtra("categoryId", categoryList[position].id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}