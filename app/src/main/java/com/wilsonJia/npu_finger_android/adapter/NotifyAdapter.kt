package com.wilsonJia.npu_finger_android.adapter

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.wilsonJia.npu_finger_android.AlarmReceiver
import com.wilsonJia.npu_finger_android.LoginActivity
import com.wilsonJia.npu_finger_android.R
import com.wilsonJia.npu_finger_android.data.courseTableData.Time
import com.wilsonJia.npu_finger_android.databinding.NotifyViewBinding
import java.util.*

var notificationId: Int = 0
class NotifyAdapter(private val itemClickListener: IItemClickListener):RecyclerView.Adapter<NotifyAdapter.MyViewHolder>(){


    var courseList: List<Time> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val notifyViewBinding =
            NotifyViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(notifyViewBinding)
    }

    private val notificationIds = mutableMapOf<Int, Int>() // 儲存通知 ID 的 map

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.notifyViewBinding.courseSwitch.text = courseList[position].courseName

        val prefs = holder.notifyViewBinding.root.context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val isChecked = prefs.getBoolean(courseList[position].courseName, false)
        holder.notifyViewBinding.courseSwitch.isChecked = isChecked

        holder.notifyViewBinding.courseSwitch.setOnClickListener {
            val courseName = courseList[position].courseName
            val courseTime = courseList[position].courseTime
            val timeList = courseTime.split("%") //拆分成["五5","二8"]
            val dayOfWeek = when(timeList[0].substring(0,1)) { //取得星期幾，例如："五"
                "一" -> Calendar.MONDAY
                "二" -> Calendar.TUESDAY
                "三" -> Calendar.WEDNESDAY
                "四" -> Calendar.THURSDAY
                "五" -> Calendar.FRIDAY
                "六" -> Calendar.SATURDAY
                "日" -> Calendar.SUNDAY
                else -> 0
            }
            val classNumberHour = when(timeList[0].substring(1)) { //取得第幾節，例如："8"
                "1" -> 8
                "2" -> 9
                "3" -> 10
                "4" -> 11
                "5" -> 13
                "6" -> 14
                "7" -> 15
                "8" -> 16
                else -> 0
            }
            val hour = classNumberHour //取得小時，例如：5
            val classNumberMinute = when(classNumberHour) { //取得第幾節，例如："8"
                1 -> 0
                2 -> 0
                3 -> 0
                4 -> 0
                5 -> 20
                6 -> 20
                7 -> 20
                8 -> 15
                else -> 0
            }

            val minute = classNumberMinute //取得分鐘，例如：8
//            val hour = timeList[0].substring(1).toInt() //取得小時，例如：5
//            val minute = timeList[1].toInt() //取得分鐘，例如：8

            if (holder.notifyViewBinding.courseSwitch.isChecked) {
                Log.d("Jia", "True")
                itemClickListener.onItemClickListener(courseList[position], true)
                prefs.edit().putBoolean(courseList[position].courseName, true).apply()
                //新增AlertDialog
                val builder = AlertDialog.Builder(holder.notifyViewBinding.root.context)
                builder.setTitle("通知新增成功")
                builder.setMessage("課程「${courseName}」已成功開啟提醒\n" +
                        "將於上課前10分鐘提醒您上課")
                builder.setPositiveButton("OK"){_, _ ->
                    //確定按鈕點擊事件
//                    if (position==0){
//                        setNotification(holder.notifyViewBinding.root.context, 23, 45, Calendar.SATURDAY,courseList[position].courseName,courseList[position].room)
//                    }else if (position==1){
//                        setNotification(holder.notifyViewBinding.root.context, 23, 48, Calendar.SATURDAY,courseList[position].courseName,courseList[position].room)
//                    }else if (position==2){
//                        setNotification(holder.notifyViewBinding.root.context, 23, 50, Calendar.SATURDAY,courseList[position].courseName,courseList[position].room)
//                    }else if (position==3){
//                        setNotification(holder.notifyViewBinding.root.context, 23, 51, Calendar.SATURDAY,courseList[position].courseName,courseList[position].room)
//                    }else if (position==4){
//                        setNotification(holder.notifyViewBinding.root.context, 23, 52, Calendar.SATURDAY,courseList[position].courseName,courseList[position].room)
//                    }
                    setNotification(holder.notifyViewBinding.root.context, hour, minute, dayOfWeek,courseList[position].courseName,courseList[position].room)
                    notificationIds[position] = notificationId
                }
                val dialog = builder.create()
                dialog.show()
            } else {
                Log.d("Jia", "False")
                itemClickListener.onItemClickListener(courseList[position], false)
                prefs.edit().putBoolean(courseList[position].courseName, false).apply()
                //新增AlertDialog
                val builder = AlertDialog.Builder(holder.notifyViewBinding.root.context)
                builder.setTitle("通知關閉成功")
                builder.setMessage("課程「${courseName}」已關閉提醒")
                builder.setPositiveButton("OK"){_, _ ->
                    //確定按鈕點擊事件


                    val notificationId = notificationIds.remove(position)
                    if (notificationId != null) {
                        cancelNotification(holder.notifyViewBinding.root.context, notificationId)
                    }
                }
                val dialog = builder.create()
                dialog.show()
            }
        }

    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    class MyViewHolder(val notifyViewBinding: NotifyViewBinding) :
        RecyclerView.ViewHolder(notifyViewBinding.root)

    interface IItemClickListener{
        fun onItemClickListener(data: Time, switch: Boolean)
    }

//    private var notificationId: Int = 0

    private fun setNotification(context: Context, hour: Int, minute: Int, dayOfWeek: Int, courseName: String ,courseRoom: String){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("courseName", courseName)
        intent.putExtra("courseRoom",courseRoom)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.DAY_OF_WEEK, dayOfWeek)
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY * 7, pendingIntent)

        // 设置通知的ID
        notificationId = (System.currentTimeMillis() / 1000).toInt()
        Log.d("Jia","Id:${notificationId}")
//        notificationId = 9527
    }

    private fun cancelNotification(context: Context,notificationId: Int){
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }

}



/*
一1 一2 一3 一4 一5 一6 一7 一8
二1 二2 二3 二4 二5 二6 二7 二8
三1 三2 三3 三4 三5 三6 三7 三8
四1 四2 四3 四4 四5 四6 四7 四8
五1 五2 五3 五4 五5 五6 五7 五8
上課時間
1 08:10
2 09:10
3 10:10
4 11:10
5 13:30
6 14:30
7 15:30
8 16:25
提醒時間
1 08:00
2 09:00
3 10:00
4 11:00
5 13:20
6 14:20
7 15:20
8 16:15
*/
