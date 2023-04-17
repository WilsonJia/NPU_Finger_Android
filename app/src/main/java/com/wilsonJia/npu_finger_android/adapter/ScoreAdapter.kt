package com.wilsonJia.npu_finger_android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wilsonJia.npu_finger_android.data.ScoreValue
import com.wilsonJia.npu_finger_android.databinding.ScoreViewBinding

class ScoreAdapter : RecyclerView.Adapter<ScoreAdapter.MyViewHolder>(){



    var scoreList: List<ScoreValue> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val scoreViewBinding =
            ScoreViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(scoreViewBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.scoreViewBinding.tvCourseName.text = scoreList[position].courseName
//        if(scoreList[position].midScore=="*"){
//            holder.scoreViewBinding.tvMidScore.text = " "
//        }else{
//            holder.scoreViewBinding.tvMidScore.text = scoreList[position].midScore
//        }
//
//        if (scoreList[position].finalScore=="*"){
//            holder.scoreViewBinding.tvFinalScore.text = "*    "
//        }else if(scoreList[position].finalScore=="P"){
//            holder.scoreViewBinding.tvFinalScore.text = "P    "
//        }else{
//            holder.scoreViewBinding.tvFinalScore.text = scoreList[position].finalScore
//        }
        holder.scoreViewBinding.tvMidScore.text = scoreList[position].midScore
        holder.scoreViewBinding.tvFinalScore.text = scoreList[position].finalScore
    }

    override fun getItemCount(): Int {
        return scoreList.size
    }

    class MyViewHolder(val scoreViewBinding: ScoreViewBinding) :
        RecyclerView.ViewHolder(scoreViewBinding.root)
}