package com.wilsonJia.npu_finger_android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wilsonJia.npu_finger_android.data.NewsInfoItem
import com.wilsonJia.npu_finger_android.databinding.NewsItemViewBinding

class NewsAdapter(private val itemClickListener:IItemClickListener): RecyclerView.Adapter<NewsAdapter.MyViewHolder>() {

    class MyViewHolder(val newsItemViewBinding: NewsItemViewBinding): RecyclerView.ViewHolder(newsItemViewBinding.root)

    var newsList: List<NewsInfoItem> = emptyList()
        set(value) {
            field=value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val newsItemViewBinding =
            NewsItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(newsItemViewBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.newsItemViewBinding.tvName.text = newsList[position].newsTitle
        holder.newsItemViewBinding.tvDate.text = newsList[position].newsDate
        holder.newsItemViewBinding.tvTeam.text = newsList[position].newsTeam

        holder.newsItemViewBinding.layoutItem.setOnClickListener {
            itemClickListener.onItemClickListener(newsList[position])
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    interface IItemClickListener{
        fun onItemClickListener(data : NewsInfoItem)
    }

}