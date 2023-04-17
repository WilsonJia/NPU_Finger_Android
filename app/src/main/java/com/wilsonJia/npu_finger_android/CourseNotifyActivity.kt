package com.wilsonJia.npu_finger_android

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.wilsonJia.npu_finger_android.Util.OkHttpUtil
import com.wilsonJia.npu_finger_android.adapter.NotifyAdapter
import com.wilsonJia.npu_finger_android.data.courseTableData.CourseTableInfo
import com.wilsonJia.npu_finger_android.data.courseTableData.Time
import com.wilsonJia.npu_finger_android.databinding.ActivityCourseNotifyBinding
import okhttp3.Response
import java.io.IOException

private lateinit var viewAdapter: NotifyAdapter
private lateinit var viewManager: RecyclerView.LayoutManager
private lateinit var binding: ActivityCourseNotifyBinding

lateinit var manager: NotificationManager
lateinit var builder : Notification.Builder

class CourseNotifyActivity() : AppCompatActivity() , NotifyAdapter.IItemClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_notify)

        binding= ActivityCourseNotifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var min = Integer.parseInt(binding.min.text.toString())

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnPlus.setOnClickListener {
            if(min!=60){
                min+=5
                binding.min.text=min.toString()
            }

        }
        binding.btnMinus.setOnClickListener {
            if(min!=5){
                min-=5
                binding.min.text=min.toString()
            }

        }

        binding.btnSave.setOnClickListener {


        }

        binding.btnRemove.setOnClickListener {
            clearNotifications()
        }


        getCourseTable()
        initView()

        binding.test.setOnClickListener {
            testNotify()
//            manager.notify(0, builder.build())
        }


    }

    private fun testNotify() {

        manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("NPU_Finger", "NPU_Finger", NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
            builder = Notification.Builder(this, "NPU_Finger")
        } else {
            builder = Notification.Builder(this)
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, Intent(this,LoginActivity::class.java), PendingIntent.FLAG_IMMUTABLE)

        builder.setSmallIcon(R.drawable.ic_stat_dolphin)
            .setContentTitle("❗️上課提醒測試❗️")
            .setContentText("課程名稱：Test\n上課地點：Test\n")
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_finger))
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)
        manager.notify(1, builder.build())
    }
    fun clearNotifications() {
        // 清除所有通知
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()

        // 清除 SharedPreference 資料
        val prefs = getSharedPreferences("com.example.npu_finger", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
        // 將所有 Switch 設定為 false

    }
    private fun getCourseTable(){
        val token= intent.getStringExtra("token")
        var TIME_TABLE_URL = "https://app.npu.edu.tw/api/coursetable?token=$token"
        OkHttpUtil.mOkHttpUtil.getAsync(TIME_TABLE_URL, object : OkHttpUtil.ICallback{

            override fun onFailure(e: IOException) {

            }

            override fun onResponse(response: Response) {

                val courseTableData = response?.body?.string()

//                Log.d("QQQ", "response: $courseTableData")

                val courseTableInfo = Gson().fromJson(courseTableData, CourseTableInfo::class.java)

//                val courseTableSize=courseTableInfo.timeList.size


                runOnUiThread {

//                    binding.tvTest.text=courseTableInfo.timeList[].courseName
                    viewAdapter.courseList=courseTableInfo.timeList

                }



            }

        })
    }

    private fun initView() {
        // 定義 LayoutManager 為 LinearLayoutManager
        viewManager = LinearLayoutManager(this)

        // 自定義 Adapte 為 MainAdapter，稍後再定義 MainAdapter 這個類別
        viewAdapter = NotifyAdapter(this)

        // 定義從佈局當中，拿到 recycler_view 元件，
        binding.recyclerView.apply {
            // 透過 kotlin 的 apply 語法糖，設定 LayoutManager 和 Adapter
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    override fun onItemClickListener(data: Time, switch: Boolean) {
        Log.d("Jia", "Course Name: "+data.courseName)
//        selectTime = data.courseTime
//        selectCourse = data.courseName
//        val list = selectTime.split("%")
//        if (switch){
//            val pref = sharedPreference(this)
//            pref.saveNotify(selectCourse,true)
//
//            courseNameList.add(selectCourse)
//            for (i in list){
////                Log.d("Jia", i)
//                timeList.add(i)
//            }
//        }else{
//            val pref = sharedPreference(this)
//            pref.saveNotify(selectCourse,false)
//
//            courseNameList.remove(selectCourse)
//            for (i in list){
////                Log.d("Jia", i)
//                timeList.remove(i)
//            }
//        }
//
////        Log.d("Jia", list.toString())
//        Log.d("Jia", "Now select : $courseNameList")
//        Log.d("Jia", "Now select : $timeList")
    }

}