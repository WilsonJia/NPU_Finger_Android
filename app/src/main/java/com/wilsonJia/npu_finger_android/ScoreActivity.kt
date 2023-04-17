package com.wilsonJia.npu_finger_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.wilsonJia.npu_finger_android.Util.OkHttpUtil
import com.wilsonJia.npu_finger_android.adapter.ScoreAdapter
import com.wilsonJia.npu_finger_android.data.ScoreInfo
import com.wilsonJia.npu_finger_android.databinding.ActivityScoreBinding
import okhttp3.Response
import java.io.IOException

class ScoreActivity : AppCompatActivity() {
    private lateinit var viewAdapter: ScoreAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var binding: ActivityScoreBinding

    private var currentYear: String = "當前學期"

    var token=""

    var year = "111"

    var sms = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val token= intent.getStringExtra("token")

        val classData =
            listOf("目前學期", "111-1","111-2","110-1", "110-2", "109-1", "109-2", "108-1", "108-2", "107-1", "107-2")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, classData)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                currentYear = binding.spinner.selectedItem.toString()
                when (currentYear) {
                    "目前學期" -> {
                        year = "111"
                        sms = "2"
                    }
                    "111-1" -> {
                        year = "111"
                        sms = "1"
                    }
                    "111-2" -> {
                        year = "111"
                        sms = "2"
                    }
                    "110-1" -> {
                        year = "110"
                        sms = "1"
                    }
                    "110-2" -> {
                        year = "110"
                        sms = "2"
                    }
                    "109-1" -> {
                        year = "109"
                        sms = "1"
                    }
                    "109-2" -> {
                        year = "109"
                        sms = "2"
                    }
                    "108-1" -> {
                        year = "108"
                        sms = "1"
                    }
                    "108-2" -> {
                        year = "108"
                        sms = "2"
                    }
                    "107-1" -> {
                        year = "107"
                        sms = "1"
                    }
                    "107-2" -> {
                        year = "107"
                        sms = "2"
                    }
                }
                getScore()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        getScore()
        initView()

//

        binding.btnBack.setOnClickListener {
            finish()
        }


    }


    fun getScore(){
        val token= intent.getStringExtra("token")
//        var SCORE_URL = "https://app.npu.edu.tw/api/score?token=$token"
        var SCORE_YEAR_URL = "https://app.npu.edu.tw/api/score?token=$token&year=$year&&sms=$sms"
        OkHttpUtil.mOkHttpUtil.getAsync(SCORE_YEAR_URL, object : OkHttpUtil.ICallback{

            override fun onFailure(e: IOException) {

            }

            override fun onResponse(response: Response) {

                val scoreData = response?.body?.string()

                Log.d("QQQ", "response: $scoreData")

                val scoreInfo = Gson().fromJson(scoreData, ScoreInfo::class.java)

                if(scoreInfo.error=="查無成績資料"){
                    Looper.prepare()
                    AlertDialog.Builder(this@ScoreActivity)
                        .setMessage("現在好像還查不到成績耶\n你要不要再確認看看")
                        .setTitle("哎呀！")
                        .setPositiveButton("好的",null )
                        .show()
                    Looper.loop()

                    runOnUiThread {
                        binding.tvAvgScore.text="總平均：0"
                        binding.tvConduct.text="操行成績：0"
                        binding.tvRank.text="班名次/班人數：0/0"


                    }
                }else{
                    runOnUiThread {
                        binding.tvAvgScore.text=scoreInfo.avgScore
                        binding.tvConduct.text=scoreInfo.conduct
                        binding.tvRank.text=scoreInfo.rank
                        binding.tvValue.text=""
                        viewAdapter.scoreList=scoreInfo.value

                    }
                }




            }

        })
    }



    private fun initView() {
        // 定義 LayoutManager 為 LinearLayoutManager
        viewManager = LinearLayoutManager(this)

        // 自定義 Adapte 為 MainAdapter，稍後再定義 MainAdapter 這個類別
        viewAdapter = ScoreAdapter()

        // 定義從佈局當中，拿到 recycler_view 元件，
        binding.recyclerView.apply {
            // 透過 kotlin 的 apply 語法糖，設定 LayoutManager 和 Adapter
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }



}