package com.example.grocerystoretest.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocerystoretest.R
import com.example.grocerystoretest.databinding.ItemBillBinding
import com.example.grocerystoretest.enums.BillStatus
import com.example.grocerystoretest.model.response.bill.BillResponse
import com.example.grocerystoretest.view.BillDetailActivity
import com.google.gson.Gson

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
        holder.bind(billResponseList[position])
    }

    inner class BillListViewHolder(val binding: ItemBillBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(billResponse: BillResponse) {
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

            binding.txtPickUpTime.text = "Lấy hàng: ${billResponse.pickUpTime}"

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

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, BillDetailActivity::class.java)
                intent.putExtra("billResponse", Gson().toJson(billResponse))
                binding.root.context.startActivity(intent)
            }
        }
    }
}