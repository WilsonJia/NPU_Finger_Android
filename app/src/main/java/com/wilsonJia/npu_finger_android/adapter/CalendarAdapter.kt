package com.wilsonJia.npu_finger_android.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.wilsonJia.npu_finger_android.R
import com.wilsonJia.npu_finger_android.data.Item
import com.wilsonJia.npu_finger_android.databinding.CalendarViewBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

var arraydata = ArrayList<String>()
var arrayeven = ArrayList<String>()
var mark = true

class CalendarAdapter: RecyclerView.Adapter<CalendarAdapter.MyViewHolder>() {

    var calendarList: List<Item> = emptyList()
        set(value) {
            field=value
            notifyDataSetChanged()
        }






    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {


        val calendarViewBinding =
            CalendarViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(calendarViewBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.calendarViewBinding.tvName.text = calendarList[position].summary
        holder.calendarViewBinding.tvDate.text = calendarList[position].start.date

        val dateStr = calendarList[position].start.date
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(dateStr)


    }

    override fun getItemCount(): Int {
//        if(calendarList.size!=0&&mark){
//            for(i in 0..calendarList.size-1){
//                arraydata.add(calendarList[i].start.date)
//                arrayeven.add(calendarList[i].summary)
//                mark = false
//            }
//        }
        return calendarList.size
    }



    class MyViewHolder(val calendarViewBinding: CalendarViewBinding) :
        RecyclerView.ViewHolder(calendarViewBinding.root)



}

