package com.wilsonJia.npu_finger_android

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.wilsonJia.npu_finger_android.Util.OkHttpUtil
import com.wilsonJia.npu_finger_android.Util.OkHttpUtil.Companion.mOkHttpUtil
import com.wilsonJia.npu_finger_android.adapter.CalendarAdapter
import com.wilsonJia.npu_finger_android.data.CalendarInfo
import com.wilsonJia.npu_finger_android.databinding.ActivityCalendarBinding
import okhttp3.Response

class CalendarActivity : AppCompatActivity() {

    private lateinit var viewAdapter: CalendarAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var binding: ActivityCalendarBinding

    var minDate = "2023-01-01T00:00:00Z"
    var maxDate = "2023-12-31T23:59:59Z"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_calendar)

        binding= ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnSchool.setOnClickListener {
            val url = "https://www.npu.edu.tw/content/index.aspx?Parser=1,9,62"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }


        initView()
        getCalendar()


    }


    fun getCalendar(){

        var CALENDAR_URL = ""

        mOkHttpUtil.getAsync(CALENDAR_URL, object : OkHttpUtil.ICallback {
            override fun onResponse(response: Response) {
                val calendarData = response?.body?.string()
                //Log.d("QQQ", "response: $calendarData")
                val calendarInfo = Gson().fromJson(calendarData, CalendarInfo::class.java)
                runOnUiThread {
                    viewAdapter.calendarList=calendarInfo.items
                }
            }

            override fun onFailure(e: okio.IOException) {

            }
        })
    }

    private fun initView(){
        viewManager = LinearLayoutManager(this)

        viewAdapter = CalendarAdapter()

        binding.recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }


    }



}