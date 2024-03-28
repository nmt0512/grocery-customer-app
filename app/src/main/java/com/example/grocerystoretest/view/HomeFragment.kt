package com.example.grocerystoretest.view

import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.grocerystoretest.R
import com.example.grocerystoretest.adapter.RecyclerViewCategoryAdapter
import com.example.grocerystoretest.adapter.RecyclerViewSearchProductAdapter
import com.example.grocerystoretest.base.BaseFragment
import com.example.grocerystoretest.databinding.FragmentHomeBinding
import com.example.grocerystoretest.model.response.product.ProductResponse
import com.example.grocerystoretest.viewmodel.CategoryViewModel
import com.example.grocerystoretest.viewmodel.ProductViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var productViewModel: ProductViewModel

    private var productResponseList = mutableListOf<ProductResponse>()

    override fun getContentLayout(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        loadingDialog?.show()
        categoryViewModel = CategoryViewModel(this.requireContext())
        productViewModel = ProductViewModel(this.requireContext())

//        val inAnimation = AnimationUtils.loadAnimation(activity, R.anim.slide_in_right)
//        val outAnimation = AnimationUtils.loadAnimation(activity, R.anim.slide_out_left)
//        binding.viewFlipper.inAnimation = inAnimation
//        binding.viewFlipper.outAnimation = outAnimation

        val slideModelImageList = mutableListOf<SlideModel>()
        slideModelImageList.add(SlideModel(R.drawable.banner1))
        slideModelImageList.add(SlideModel(R.drawable.banner2))
        slideModelImageList.add(SlideModel(R.drawable.banner3))
        slideModelImageList.add(SlideModel(R.drawable.banner4))
        slideModelImageList.add(SlideModel(R.drawable.banner5))
        slideModelImageList.add(SlideModel(R.drawable.banner6))
        slideModelImageList.add(SlideModel(R.drawable.banner7))
        binding.sliderBanner.setImageList(slideModelImageList, ScaleTypes.CENTER_CROP)

        binding.rvCategory.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecommendedProduct.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvBestSellingProduct.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.rvSearchProduct.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.rvSearchProduct.adapter = RecyclerViewSearchProductAdapter(productResponseList)
    }

    override fun initListener() {
        binding.searchView.setOnClickListener {
            (this.activity as HomeActivity).binding.bottomNavigationBar.visibility = View.GONE
            binding.searchView.isIconified = false
            binding.nscSearchProduct.visibility = View.VISIBLE
        }
        binding.searchView.setOnCloseListener {
            (this.activity as HomeActivity).binding.bottomNavigationBar.visibility = View.VISIBLE
            binding.nscSearchProduct.visibility = View.GONE
            productResponseList.clear()
            binding.rvSearchProduct.adapter?.notifyDataSetChanged()
            return@setOnCloseListener false
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { query ->
                    productViewModel.searchProduct(query)
                    productViewModel.searchProductResponseListLiveData.observe(this@HomeFragment) {
                        productResponseList.clear()
                        productResponseList.addAll(it)
                        binding.rvSearchProduct.adapter?.notifyDataSetChanged()
                    }
                }
                return false
            }
        })
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