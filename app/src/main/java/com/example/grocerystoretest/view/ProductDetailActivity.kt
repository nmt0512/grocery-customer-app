package com.example.grocerystoretest.view

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.grocerystoretest.R
import com.example.grocerystoretest.adapter.RecyclerViewSimilarProductAdapter
import com.example.grocerystoretest.base.BaseActivity
import com.example.grocerystoretest.databinding.ActivityProductDetailBinding
import com.example.grocerystoretest.model.request.cart.AddToCartRequest
import com.example.grocerystoretest.model.response.cart.AddToCartResponse
import com.example.grocerystoretest.utils.NumberConverterUtil
import com.example.grocerystoretest.viewmodel.CartViewModel
import com.example.grocerystoretest.viewmodel.ProductViewModel

class ProductDetailActivity : BaseActivity<ActivityProductDetailBinding>() {

    private lateinit var productViewModel: ProductViewModel
    private lateinit var cartViewModel: CartViewModel

    private var productId = 0

    override fun getContentLayout(): Int {
        return R.layout.activity_product_detail
    }

    override fun initView() {
        loadingDialog?.show()
        productId = intent.getIntExtra("productId", 0)
        (productId != 0).let {
            productViewModel = ProductViewModel(this)
            cartViewModel = CartViewModel(this)
            productViewModel.getProductDetailById(productId)
            binding.txtAddingCartQuantity.text = 1.toString()
        }
        binding.rvSimilarProduct.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun observeData() {
        productViewModel.productDetailLiveData.observe(this) { productResponse ->
            val slideModelImageList = productResponse.images.map { SlideModel(it) }
            binding.sliderProductImage.setImageList(slideModelImageList, ScaleTypes.CENTER_INSIDE)

            binding.txtProductName.text = productResponse.name
            binding.txtProductPrice.text =
                NumberConverterUtil.getProductPriceStringByPrice(productResponse.unitPrice)
            binding.txtProductQuantity.text = "Số lượng: ${productResponse.quantity}"
            binding.expandableTxtProductDescription.text = productResponse.description

            loadingDialog?.dismiss()
        }

        productViewModel.getSimilarProduct(productId).observe(this) {
            binding.rvSimilarProduct.adapter = RecyclerViewSimilarProductAdapter(it)
        }

        cartViewModel.addToCartResponseLiveData.observe(this) {
            if (it != AddToCartResponse()) {
                Toast.makeText(
                    this,
                    "Thêm sản phẩm vào giỏ hàng thành công",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Sản phẩm đã hết hàng",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun initListener() {
        binding.btnMinusQuantity.setOnClickListener {
            val oldQuantity = binding.txtAddingCartQuantity.text.toString().toInt()
            if (oldQuantity > 1) {
                binding.txtAddingCartQuantity.text = (oldQuantity - 1).toString()
            }
        }
        binding.btnPlusQuantity.setOnClickListener {
            val oldQuantity = binding.txtAddingCartQuantity.text.toString().toInt()
            binding.txtAddingCartQuantity.text = (oldQuantity + 1).toString()
        }
        binding.btnAddToCart.setOnClickListener {
            val request =
                AddToCartRequest(productId, binding.txtAddingCartQuantity.text.toString().toInt())
            cartViewModel.addToCart(request)
        }
    }

}