package com.example.grocerystoretest.view

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerystoretest.R
import com.example.grocerystoretest.adapter.RecyclerViewBillDetailAdapter
import com.example.grocerystoretest.base.BaseActivity
import com.example.grocerystoretest.databinding.ActivityBillDetailBinding
import com.example.grocerystoretest.enums.BillStatus
import com.example.grocerystoretest.model.response.bill.BillResponse
import com.example.grocerystoretest.utils.NumberConverterUtil
import com.google.gson.Gson

class BillDetailActivity : BaseActivity<ActivityBillDetailBinding>() {
    override fun getContentLayout(): Int {
        return R.layout.activity_bill_detail
    }

    override fun initView() {
        binding.rvBillItem.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val billResponseStr = intent.getStringExtra("billResponse")
        val billResponse = Gson().fromJson(billResponseStr, BillResponse::class.java)
        val billItemResponseList = billResponse.billItems
        billItemResponseList?.let {
            binding.rvBillItem.adapter = RecyclerViewBillDetailAdapter(it)
        }

        binding.txtTotalPrice.text =
            "Tổng tiền: ${NumberConverterUtil.convertNumberToStringWithDot(billResponse.totalPrice!!)} Đ"
        binding.txtOrderTime.text = "Đặt hàng lúc: ${billResponse.createdDate}"
        binding.txtPickUpTime.text = "Lấy hàng lúc: ${billResponse.pickUpTime}"
        billResponse.status?.let {
            if (it == BillStatus.PREPARED) {
                binding.txtBillStatus.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.yellow)
                )
            } else if (it == BillStatus.COMPLETED) {
                binding.txtBillStatus.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.green)
                )
            }
            binding.txtBillStatus.text = it.description
        }
    }

    override fun initListener() {
        binding.btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun observeData() {

    }
}