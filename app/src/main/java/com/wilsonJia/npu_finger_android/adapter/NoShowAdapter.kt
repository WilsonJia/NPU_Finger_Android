package com.wilsonJia.npu_finger_android.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wilsonJia.npu_finger_android.data.NoShowValue
import com.wilsonJia.npu_finger_android.databinding.NoshowViewBinding

var noShowCount = ArrayList<Int>()

class NoShowAdapter : RecyclerView.Adapter<NoShowAdapter.MyViewHolder>(){

    var leave = 0
    var absent = 0


    var noShowList: List<NoShowValue> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val noshowViewBinding =
            NoshowViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(noshowViewBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.noshowViewBinding.tvDate.text = noShowList[position].date
        if (noShowList[position].course1=="病假"){
            holder.noshowViewBinding.course1.text = "病"
        }else if (noShowList[position].course1=="公假"){
            holder.noshowViewBinding.course1.text = "公"
        }else if (noShowList[position].course1=="生理假"){
            holder.noshowViewBinding.course1.text = "生"
        }else if (noShowList[position].course1=="喪假"){
            holder.noshowViewBinding.course1.text = "喪"
        }else if (noShowList[position].course1=="缺曠"){
            holder.noshowViewBinding.course1.text = "曠"
        }else{
            holder.noshowViewBinding.course1.text = "　"
        }
        if (noShowList[position].course2=="病假"){
            holder.noshowViewBinding.course2.text = "病"
        }else if (noShowList[position].course2=="公假"){
            holder.noshowViewBinding.course2.text = "公"
        }else if (noShowList[position].course2=="生理假"){
            holder.noshowViewBinding.course2.text = "生"
        }else if (noShowList[position].course2=="喪假"){
            holder.noshowViewBinding.course2.text = "喪"
        }else if (noShowList[position].course2=="缺曠"){
            holder.noshowViewBinding.course2.text = "曠"
        }else{
            holder.noshowViewBinding.course2.text = "　"
        }
        if (noShowList[position].course3=="病假"){
            holder.noshowViewBinding.course3.text = "病"
        }else if (noShowList[position].course3=="公假"){
            holder.noshowViewBinding.course3.text = "公"
        }else if (noShowList[position].course3=="生理假"){
            holder.noshowViewBinding.course3.text = "生"
        }else if (noShowList[position].course3=="喪假"){
            holder.noshowViewBinding.course3.text = "喪"
        }else if (noShowList[position].course3=="缺曠"){
            holder.noshowViewBinding.course3.text = "曠"
        }else{
            holder.noshowViewBinding.course3.text = "　"
        }
        if (noShowList[position].course4=="病假"){
            holder.noshowViewBinding.course4.text = "病"
        }else if (noShowList[position].course4=="公假"){
            holder.noshowViewBinding.course4.text = "公"
        }else if (noShowList[position].course4=="生理假"){
            holder.noshowViewBinding.course4.text = "生"
        }else if (noShowList[position].course4=="喪假"){
            holder.noshowViewBinding.course4.text = "喪"
        }else if (noShowList[position].course4=="缺曠"){
            holder.noshowViewBinding.course4.text = "曠"
        }else{
            holder.noshowViewBinding.course4.text = "　"
        }
        if (noShowList[position].course5=="病假"){
            holder.noshowViewBinding.course5.text = "病"
        }else if (noShowList[position].course5=="公假"){
            holder.noshowViewBinding.course5.text = "公"
        }else if (noShowList[position].course5=="生理假"){
            holder.noshowViewBinding.course5.text = "生"
        }else if (noShowList[position].course5=="喪假"){
            holder.noshowViewBinding.course5.text = "喪"
        }else if (noShowList[position].course5=="缺曠"){
            holder.noshowViewBinding.course5.text = "曠"
        }else{
            holder.noshowViewBinding.course5.text = "　"
        }
        if (noShowList[position].course6=="病假"){
            holder.noshowViewBinding.course6.text = "病"
        }else if (noShowList[position].course6=="公假"){
            holder.noshowViewBinding.course6.text = "公"
        }else if (noShowList[position].course6=="生理假"){
            holder.noshowViewBinding.course6.text = "生"
        }else if (noShowList[position].course6=="喪假"){
            holder.noshowViewBinding.course6.text = "喪"
        }else if (noShowList[position].course6=="缺曠"){
            holder.noshowViewBinding.course6.text = "曠"
        }else{
            holder.noshowViewBinding.course6.text = "　"
        }
        if (noShowList[position].course7=="病假"){
            holder.noshowViewBinding.course7.text = "病"
        }else if (noShowList[position].course7=="公假"){
            holder.noshowViewBinding.course7.text = "公"
        }else if (noShowList[position].course7=="生理假"){
            holder.noshowViewBinding.course7.text = "生"
        }else if (noShowList[position].course7=="喪假"){
            holder.noshowViewBinding.course7.text = "喪"
        }else if (noShowList[position].course7=="缺曠"){
            holder.noshowViewBinding.course7.text = "曠"
        }else{
            holder.noshowViewBinding.course7.text = "　"
        }
        if (noShowList[position].course8=="病假"){
            holder.noshowViewBinding.course8.text = "病"
        }else if (noShowList[position].course8=="公假"){
            holder.noshowViewBinding.course8.text = "公"
        }else if (noShowList[position].course8=="生理假"){
            holder.noshowViewBinding.course8.text = "生"
        }else if (noShowList[position].course8=="喪假"){
            holder.noshowViewBinding.course8.text = "喪"
        }else if (noShowList[position].course8=="缺曠"){
            holder.noshowViewBinding.course8.text = "曠"
        }else{
            holder.noshowViewBinding.course8.text = "　"
        }

//        Log.d("Jia","$absent   $leave")
//        noShowCount = arrayListOf(absent, leave)
//        Log.d("Jia","$noShowCount")
//        holder.noshowViewBinding.course1.text = noShowList[position].course1
//        holder.noshowViewBinding.course2.text = noShowList[position].course2
//        holder.noshowViewBinding.course3.text = noShowList[position].course3
//        holder.noshowViewBinding.course4.text = noShowList[position].course4
//        holder.noshowViewBinding.course5.text = noShowList[position].course5
//        holder.noshowViewBinding.course6.text = noShowList[position].course6
//        holder.noshowViewBinding.course7.text = noShowList[position].course7
//        holder.noshowViewBinding.course8.text = noShowList[position].course8


    }

    override fun getItemCount(): Int {
        return noShowList.size
    }

    fun getAbsentCount(): Int {
        return absent
    }

    fun getLeaveCount(): Int {
        return leave
    }

    fun getNoShowCount():ArrayList<Int> {

//        noShowCount.add(absent, leave)
        Log.d("Jia","$noShowCount")
        return noShowCount
    }

    class MyViewHolder(val noshowViewBinding: NoshowViewBinding) :
        RecyclerView.ViewHolder(noshowViewBinding.root)

}