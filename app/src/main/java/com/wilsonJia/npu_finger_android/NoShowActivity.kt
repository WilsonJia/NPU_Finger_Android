package com.wilsonJia.npu_finger_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.wilsonJia.npu_finger_android.Util.OkHttpUtil
import com.wilsonJia.npu_finger_android.adapter.NoShowAdapter
import com.wilsonJia.npu_finger_android.data.NoShowInfo
import com.wilsonJia.npu_finger_android.databinding.ActivityNoShowBinding
import okhttp3.Response
import java.io.IOException

class NoShowActivity : AppCompatActivity() {

    private lateinit var viewAdapter: NoShowAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var binding: ActivityNoShowBinding

    var token= ""
    var leave=0
    var absent=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_no_show)

        binding = ActivityNoShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        getNoShow()


        binding.btnBack.setOnClickListener {
            finish()
        }

//        getCount()
    }


    fun getNoShow(){
        val token= intent.getStringExtra("token")
        var NO_SHOW_URL = "https://app.npu.edu.tw/api/noshow?token=$token"
//        var NO_SHOW_URL = "http://npuia.npu.edu.tw/getNoShow1?token=$token"
        OkHttpUtil.mOkHttpUtil.getAsync(NO_SHOW_URL, object : OkHttpUtil.ICallback{

            override fun onFailure(e: IOException) {

            }

            override fun onResponse(response: Response) {

                val noShowData = response?.body?.string()

                Log.d("QQQ", "response: $noShowData")

                val noShowInfo = Gson().fromJson(noShowData, NoShowInfo::class.java)

                if(noShowInfo.error=="查無缺曠資料") {
                    Looper.prepare()
                    AlertDialog.Builder(this@NoShowActivity)
                        .setMessage("查無缺曠資料！\n看來是個不翹課的乖學生")
                        .setTitle("哎呀❗")
                        .setPositiveButton("好的",null )
                        .show()
                    Looper.loop()


                }else {

                    val size = noShowInfo.value.lastIndex
                    for (i in 0..size){
//                        Log.d("Jia","${i} ${size}")
                        if (noShowInfo.value[i].course1=="病假"||noShowInfo.value[i].course1=="公假"||noShowInfo.value[i].course1=="生理假"||noShowInfo.value[i].course1=="喪假"){
                            leave++
                        }else if (noShowInfo.value[i].course1=="缺曠"){
                            absent++
                        }
                        if (noShowInfo.value[i].course2=="病假"||noShowInfo.value[i].course2=="公假"||noShowInfo.value[i].course2=="生理假"||noShowInfo.value[i].course2=="喪假"){
                            leave++
                        } else if (noShowInfo.value[i].course2=="缺曠"){
                            absent++
                        }
                        if (noShowInfo.value[i].course3=="病假"||noShowInfo.value[i].course3=="公假"||noShowInfo.value[i].course3=="生理假"||noShowInfo.value[i].course3=="喪假"){
                            leave++
                        }else if (noShowInfo.value[i].course3=="缺曠"){
                            absent++
                        }
                        if (noShowInfo.value[i].course4=="病假"||noShowInfo.value[i].course4=="公假"||noShowInfo.value[i].course4=="生理假"||noShowInfo.value[i].course4=="喪假"){
                            leave++
                        }else if (noShowInfo.value[i].course4=="缺曠"){
                            absent++
                        }
                        if (noShowInfo.value[i].course5=="病假"||noShowInfo.value[i].course5=="公假"||noShowInfo.value[i].course5=="生理假"||noShowInfo.value[i].course5=="喪假"){
                            leave++
                        }else if (noShowInfo.value[i].course5=="缺曠"){
                            absent++
                        }
                        if (noShowInfo.value[i].course6=="病假"||noShowInfo.value[i].course6=="公假"||noShowInfo.value[i].course6=="生理假"||noShowInfo.value[i].course6=="喪假"){
                            leave++
                        }else if (noShowInfo.value[i].course6=="缺曠"){
                            absent++
                        }
                        if (noShowInfo.value[i].course7=="病假"||noShowInfo.value[i].course7=="公假"||noShowInfo.value[i].course7=="生理假"||noShowInfo.value[i].course7=="喪假"){
                            leave++
                        }else if (noShowInfo.value[i].course7=="缺曠"){
                            absent++
                        }
                        if (noShowInfo.value[i].course8=="病假"||noShowInfo.value[i].course8=="公假"||noShowInfo.value[i].course8=="生理假"||noShowInfo.value[i].course8=="喪假"){
                            leave++
                        }else if (noShowInfo.value[i].course8=="缺曠"){
                            absent++
                        }
                        Log.d("Jia","$absent   $leave")
                    }
                    runOnUiThread {
                        viewAdapter.noShowList=noShowInfo.value
                        binding.tvAbsent.text="曠課節數：$absent"
                        binding.tvLeave.text="已請節數：$leave"
                    }

                }




            }

        })
    }



    private fun initView() {
        // 定義 LayoutManager 為 LinearLayoutManager
        viewManager = LinearLayoutManager(this)

        // 自定義 Adapte 為 MainAdapter，稍後再定義 MainAdapter 這個類別
        viewAdapter = NoShowAdapter()

        // 定義從佈局當中，拿到 recycler_view 元件，
        binding.recyclerView.apply {
            // 透過 kotlin 的 apply 語法糖，設定 LayoutManager 和 Adapter
            layoutManager = viewManager
            adapter = viewAdapter

        }

    }


}