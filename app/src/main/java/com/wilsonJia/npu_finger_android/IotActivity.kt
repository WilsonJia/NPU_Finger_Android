package com.wilsonJia.npu_finger_android

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.wilsonJia.npu_finger_android.data.iotInfo
import com.wilsonJia.npu_finger_android.databinding.ActivityIotBinding
import okhttp3.*
import java.io.IOException
import java.util.*
import kotlin.concurrent.schedule

class IotActivity : AppCompatActivity() {
    var Temperature = 0.0
    var Humidity = 0.0
    var Quality = 0.0
    var notiValue = false
    lateinit var manager: NotificationManager
    lateinit var builder: Notification.Builder

    private lateinit var binding: ActivityIotBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iot)
        binding = ActivityIotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getThingSpeak()
        noti()
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    fun noti() {
        manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("NPU Finger", "空氣不好！快點開窗戶", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
            builder = Notification.Builder(this, "NPU Finger")
        } else {
            builder = Notification.Builder(this)
        }

        builder.setSmallIcon(R.drawable.ic_stat_dolphin)
            .setContentTitle("NPU Finger")
            .setContentText("通知！教室位置空氣狀況極差，請開窗！")
            .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.ic_stat_dolphin))
            .setAutoCancel(true)
    }

    private fun getThingSpeak() {
        val Url = "https://api.thingspeak.com/channels/1784100/feeds.json?results=1"

        val okhttpClient = OkHttpClient().newBuilder().build()

        var request = Request.Builder().url(Url).get().build()

        val call = okhttpClient.newCall(request)

        call.enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Daniel","onFailure $e")
            }
            override fun onResponse(call: okhttp3.Call, response: Response) {
                request = Request.Builder().url(Url).get().build()
                val SensorData = response.body?.string()

                val SensorInfo = Gson().fromJson(SensorData, iotInfo::class.java)

                for(i in SensorInfo.feeds){
                    Temperature = i.field3.toDouble()
                    Humidity = i.field2.toDouble()
                    Quality = i.field1.toDouble()
//                    Log.d("Daniel","type ${i.field1}")
//                    Log.d("Daniel","type ${i.field2}")
//                    Log.d("Daniel","type ${i.field3}")

                    runOnUiThread {
                        binding.TemperatureView.text = Temperature.toString()+"°C"
                        binding.HumidityView.text = Humidity.toString()+"%"
                        if(Quality==1.0){
                            binding.QualityView.text = "Excellent"
                            notiValue = false
                        }else if(Quality==2.0){
                            binding.QualityView.text = "Good"
                            notiValue = false
                        }else if(Quality==3.0){
                            binding.QualityView.text = "Average"
                            notiValue = false
                        }else if(Quality==4.0){
                            binding.QualityView.text = "Fair"
                            if(notiValue!=true){
                                manager.notify(0,builder.build())
                            }
                            notiValue = true
                        }else if(Quality==5.0) {
                            binding.QualityView.text = "Poor"
                            if(notiValue!=true){
                                manager.notify(0,builder.build())
                            }
                            notiValue = true
                        }
                    }
                }

            }
        })
        Timer().schedule(10000) {
            getThingSpeak()
        }
    }
}