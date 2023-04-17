package com.wilsonJia.npu_finger_android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wilsonJia.npu_finger_android.data.RewardValue
import com.wilsonJia.npu_finger_android.databinding.RewardViewBinding

class RewardAdapter(private val itemClickListener: IItemClickListener) :RecyclerView.Adapter<RewardAdapter.MyViewHolder>(){



    var rewardList: List<RewardValue> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val rewardViewBinding =
            RewardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(rewardViewBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.rewardViewBinding.tvCategory.text = rewardList[position].category
        holder.rewardViewBinding.tvCount.text = rewardList[position].count
        holder.rewardViewBinding.tvDate.text = rewardList[position].date

        holder.rewardViewBinding.layoutItem2.setOnClickListener {
            itemClickListener.onItemClickListener(rewardList[position])
        }
        //holder.rewardViewBinding.tvInfo.text = rewardList[position].info

    }

    override fun getItemCount(): Int {
        return rewardList.size
    }

    interface IItemClickListener {
        fun onItemClickListener(data: RewardValue)
    }

    class MyViewHolder(val rewardViewBinding: RewardViewBinding) :
        RecyclerView.ViewHolder(rewardViewBinding.root)
}

