package com.wilsonJia.npu_finger_android

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.wilsonJia.npu_finger_android.Util.OkHttpUtil
import com.wilsonJia.npu_finger_android.adapter.RewardAdapter
import com.wilsonJia.npu_finger_android.data.RewardInfo
import com.wilsonJia.npu_finger_android.data.RewardValue
import com.wilsonJia.npu_finger_android.databinding.ActivityRewardBinding
import okhttp3.Response
import java.io.IOException

private lateinit var viewAdapter: RewardAdapter
private lateinit var binding: ActivityRewardBinding
private lateinit var viewManager: RecyclerView.LayoutManager
private var currentYear: String = "當前學期"

var year = "111"

var sms = "2"

class RewardActivity : AppCompatActivity(),RewardAdapter.IItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_reward)

        binding = ActivityRewardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val classData =
            listOf("目前學期","111-1", "111-2","110-1", "110-2", "109-1", "109-2", "108-1", "108-2", "107-1", "107-2")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, classData)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                getReward()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

//        getReward()
        initView()
        binding.btnBack.setOnClickListener {
            finish()
        }

    }
    fun getReward(){
        val token= intent.getStringExtra("token")
        var REWARD_YEAR_URL = "https://app.npu.edu.tw/api/reward?token=$token&year=$year&&sms=$sms"
        print(REWARD_YEAR_URL)
        OkHttpUtil.mOkHttpUtil.getAsync(REWARD_YEAR_URL, object : OkHttpUtil.ICallback{

            override fun onFailure(e: IOException) {
                Log.d("Jiayu","response: fail")
            }

            override fun onResponse(response: Response) {
                val rewardData = response?.body?.string()
                Log.d("QQQ", "response: $rewardData")
                val rewardInfo = Gson().fromJson(rewardData, RewardInfo::class.java)


                if (rewardInfo.status == "沒有獎懲紀錄"){
                    runOnUiThread {
                        binding.textView.text = "沒有獎懲資料！"

                    }
                    Looper.prepare()
                    AlertDialog.Builder(this@RewardActivity)
                        .setMessage("看來是個不犯法的乖小孩呢！")
                        .setTitle("哎呀！Oops!")
                        .setPositiveButton("Ok",null)
                        .show()
                    Looper.loop()
                }else{
                    runOnUiThread {
                        binding.textView.text = "唉呦！這學期有被記功！"
                        viewAdapter.rewardList= rewardInfo.value

                    }
                }
            }

        })
    }
    private fun initView() {
        // 定義 LayoutManager 為 LinearLayoutManager
        viewManager = LinearLayoutManager(this)

        // 自定義 Adapte 為 MainAdapter，稍後再定義 MainAdapter 這個類別
        viewAdapter = RewardAdapter(this)

        // 定義從佈局當中，拿到 recycler_view 元件，
        binding.recyclerView.apply {
            // 透過 kotlin 的 apply 語法糖，設定 LayoutManager 和 Adapter
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    override fun onItemClickListener(data: RewardValue) {
        AlertDialog.Builder(this@RewardActivity)
            .setMessage("日期：${data.date}\n" +
                    "數量：${data.count}\n"+
                    "事由內容：${data.info}")
            .setTitle("${data.category}\n")
            .setPositiveButton("Ok",null)
            .show()
    }
}