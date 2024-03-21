package com.example.grocerystoretest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocerystoretest.R
import com.example.grocerystoretest.databinding.ItemBillBinding
import com.example.grocerystoretest.model.response.bill.BillResponse
import com.example.grocerystoretest.utils.NumberConverterUtil

class RecyclerViewBillListAdapter(private val billResponseList: List<BillResponse>) :
    RecyclerView.Adapter<RecyclerViewBillListAdapter.BillListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bill, parent, false)
        val binding = ItemBillBinding.bind(view)
        return BillListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return billResponseList.size
    }

    override fun onBindViewHolder(holder: BillListViewHolder, position: Int) {
        holder.bind(billResponseList[position], position)
    }

    inner class BillListViewHolder(val binding: ItemBillBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(billResponse: BillResponse, position: Int) {
            if (position == 0) {
                binding.root.background = null
            }
            val billItemList = billResponse.billItems!!
            Glide.with(binding.root)
                .load(billItemList[0].productResponse.images[0])
                .centerCrop()
                .into(binding.imageViewBill)

            val firstBillItem = billItemList.first()
            binding.txtProduct1.text =
                "${firstBillItem.quantity}x ${firstBillItem.productResponse.name}"

            if (billItemList.size > 1) {
                val secondBillItem = billItemList[1]
                binding.txtProduct2.visibility = View.VISIBLE
                binding.txtProduct2.text =
                    "${secondBillItem.quantity}x ${secondBillItem.productResponse.name}"
                if (billItemList.size > 2) {
                    binding.txtOtherProductNumber.visibility = View.VISIBLE
                    binding.txtOtherProductNumber.text = "+ ${billItemList.size - 2}"
                }
            }

            binding.txtTotalPrice.text = "${NumberConverterUtil.getProductPriceStringByPrice(billResponse.totalPrice!!)}"
            binding.txtBillStatus.text = billResponse.status?.description
        }
    }
}