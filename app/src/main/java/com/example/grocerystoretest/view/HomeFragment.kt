package com.example.grocerystoretest.view

import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerystoretest.R
import com.example.grocerystoretest.adapter.RecyclerViewCategoryAdapter
import com.example.grocerystoretest.base.BaseFragment
import com.example.grocerystoretest.databinding.FragmentHomeBinding
import com.example.grocerystoretest.viewmodel.CategoryViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private lateinit var categoryViewModel: CategoryViewModel

    override fun getContentLayout(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        loadingDialog?.show()
        categoryViewModel = CategoryViewModel(this.requireContext())

        val inAnimation = AnimationUtils.loadAnimation(activity, R.anim.slide_in_right)
        val outAnimation = AnimationUtils.loadAnimation(activity, R.anim.slide_out_left)
        binding.viewFlipper.inAnimation = inAnimation
        binding.viewFlipper.outAnimation = outAnimation

        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
        }

        binding.rvCategory.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecommendedProduct.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvBestSellingProduct.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun initListener() {

    }

    override fun observeLiveData() {
        categoryViewModel.getCategoryResponseList()
        if (categoryViewModel.categoryResponseListLiveData.value != null) {
            loadingDialog?.dismiss()
        }
        categoryViewModel.categoryResponseListLiveData.observe(this) {
            if (it != null && it.isNotEmpty()) {
                binding.rvCategory.adapter = RecyclerViewCategoryAdapter(it)
            } else {
                Toast.makeText(activity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
            }
            loadingDialog?.dismiss()
        }

    }

}