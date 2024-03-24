package com.example.grocerystoretest.view

import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.grocerystoretest.R
import com.example.grocerystoretest.adapter.RecyclerViewProductAdapter
import com.example.grocerystoretest.base.BaseActivity
import com.example.grocerystoretest.databinding.ActivityProductListBinding
import com.example.grocerystoretest.databinding.DialogAddToCartBinding
import com.example.grocerystoretest.model.request.cart.AddToCartRequest
import com.example.grocerystoretest.model.response.cart.AddToCartResponse
import com.example.grocerystoretest.model.response.product.ProductResponse
import com.example.grocerystoretest.utils.NumberConverterUtil
import com.example.grocerystoretest.viewmodel.CartViewModel
import com.example.grocerystoretest.viewmodel.ProductViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProductListActivity : BaseActivity<ActivityProductListBinding>(), IProductListActivity {

    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var dialogAddToCartBinding: DialogAddToCartBinding

    private lateinit var productViewModel: ProductViewModel
    private lateinit var cartViewModel: CartViewModel

    private var categoryId = 0
    private val pageSize = 8
    private var pageNumber = 1

    private val productResponseList = mutableListOf<ProductResponse>()

    private var isAbleToFetchMore = true
    private var addingToCartProductId = 0

    override fun getContentLayout(): Int {
        return R.layout.activity_product_list
    }

    override fun initView() {
        initBottomSheetDialog()
        productViewModel = ProductViewModel(this)
        cartViewModel = CartViewModel(this)
        binding.rvProduct.layoutManager = GridLayoutManager(this, 2)

        categoryId = intent.getIntExtra("categoryId", 0)
        if (categoryId != 0) {
            binding.pbLoading.visibility = View.INVISIBLE
            loadingDialog?.show()
        }
    }

    override fun initListener() {
        binding.btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
        binding.nsRvProduct.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (isAbleToFetchMore) {
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                    pageNumber++
                    binding.pbLoading.visibility = View.VISIBLE
                    observeData()
                }
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
                        this,
                        "Thêm sản phẩm vào giỏ hàng thành công",
                        Toast.LENGTH_SHORT
                    ).show()
                    bottomSheetDialog.hide()
                } else {
                    Toast.makeText(
                        this,
                        "Sản phẩm đã hết hàng",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun observeData() {
        if (categoryId != 0) {
            productViewModel.getProductResponseListByCategoryIdPaging(
                categoryId,
                pageNumber,
                pageSize
            ).observe(this) {
                if (it != null) {
                    if (it.isNotEmpty()) {
                        productResponseList.addAll(it)
                        binding.rvProduct.adapter =
                            RecyclerViewProductAdapter(this, productResponseList)
                        loadingDialog?.dismiss()
                        if (it.size < pageSize) {
                            disableFetchMore()
                        }
                    } else {
                        disableFetchMore()
                    }
                } else {
                    Toast.makeText(
                        this@ProductListActivity,
                        "Fail to fetch data",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    private fun initBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetDialogView = LayoutInflater
            .from(this)
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

    private fun disableFetchMore() {
        isAbleToFetchMore = false
        binding.pbLoading.visibility = View.GONE
    }

}