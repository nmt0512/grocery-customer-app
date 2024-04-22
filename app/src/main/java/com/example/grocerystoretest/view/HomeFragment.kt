package com.example.grocerystoretest.view

import android.view.LayoutInflater
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.grocerystoretest.R
import com.example.grocerystoretest.adapter.RecyclerViewHomeProductAdapter
import com.example.grocerystoretest.adapter.RecyclerViewCategoryAdapter
import com.example.grocerystoretest.adapter.RecyclerViewSearchProductAdapter
import com.example.grocerystoretest.base.BaseFragment
import com.example.grocerystoretest.databinding.DialogAddToCartBinding
import com.example.grocerystoretest.databinding.FragmentHomeBinding
import com.example.grocerystoretest.model.request.cart.AddToCartRequest
import com.example.grocerystoretest.model.response.cart.AddToCartResponse
import com.example.grocerystoretest.model.response.product.ProductResponse
import com.example.grocerystoretest.utils.NumberConverterUtil
import com.example.grocerystoretest.viewmodel.CartViewModel
import com.example.grocerystoretest.viewmodel.CategoryViewModel
import com.example.grocerystoretest.viewmodel.ProductViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class HomeFragment : BaseFragment<FragmentHomeBinding>(), IHomeFragment {

    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var productViewModel: ProductViewModel
    private lateinit var cartViewModel: CartViewModel

    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var dialogAddToCartBinding: DialogAddToCartBinding

    private var productResponseList = mutableListOf<ProductResponse>()

    private var addingToCartProductId = 0

    override fun getContentLayout(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        loadingDialog?.show()

        initBottomSheetDialog()

        categoryViewModel = CategoryViewModel(this.requireContext())
        productViewModel = ProductViewModel(this.requireContext())
        cartViewModel = CartViewModel(this.requireContext())

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

        dialogAddToCartBinding.btnMinusQuantity.setOnClickListener {
            val oldQuantity = dialogAddToCartBinding.txtQuantity.text.toString().toInt()
            if (oldQuantity > 1) {
                dialogAddToCartBinding.txtQuantity.text = (oldQuantity - 1).toString()
            }
        }
        dialogAddToCartBinding.btnPlusQuantity.setOnClickListener {
            val oldQuantity = dialogAddToCartBinding.txtQuantity.text.toString().toInt()
            dialogAddToCartBinding.txtQuantity.text = (oldQuantity + 1).toString()
        }
        dialogAddToCartBinding.btnAddToCart.setOnClickListener {
            val quantity = dialogAddToCartBinding.txtQuantity.text.toString().toInt()
            val addToCartRequest = AddToCartRequest(addingToCartProductId, quantity)
            cartViewModel.addToCart(addToCartRequest)
            cartViewModel.addToCartResponseLiveData.observe(this) {
                if (it != AddToCartResponse()) {
                    Toast.makeText(
                        this.requireContext(),
                        "Thêm sản phẩm vào giỏ hàng thành công",
                        Toast.LENGTH_SHORT
                    ).show()
                    bottomSheetDialog.hide()
                } else {
                    Toast.makeText(
                        this.requireContext(),
                        "Sản phẩm đã hết hàng",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
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

        productViewModel.getBestSellingProduct().observe(this) {
            if (it.isNotEmpty()) {
                binding.rvBestSellingProduct.adapter = RecyclerViewHomeProductAdapter(this, it)
            }
        }

    }

    private fun initBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(this.requireContext())
        val bottomSheetDialogView = LayoutInflater
            .from(this.requireContext())
            .inflate(R.layout.dialog_add_to_cart, null)
        dialogAddToCartBinding = DialogAddToCartBinding.bind(bottomSheetDialogView)
        bottomSheetDialog.setContentView(bottomSheetDialogView)
    }

    override fun showBottomSheetDialog(productResponse: ProductResponse) {
        if (productResponse.images.isNotEmpty()) {
            Glide.with(this)
                .load(productResponse.images[0])
                .centerInside()
                .into(dialogAddToCartBinding.imageViewProduct)
        }
        dialogAddToCartBinding.txtProductName.text = productResponse.name
        val priceStr = NumberConverterUtil.getProductPriceStringByPrice(productResponse.unitPrice)
        dialogAddToCartBinding.txtProductPrice.text = priceStr
        dialogAddToCartBinding.txtQuantity.text = 1.toString()
        addingToCartProductId = productResponse.id
        bottomSheetDialog.show()
    }

}