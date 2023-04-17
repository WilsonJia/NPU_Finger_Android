package com.wilsonJia.npu_finger_android

import android.R
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.wilsonJia.npu_finger_android.data.GreaduateInfo
import com.wilsonJia.npu_finger_android.databinding.ActivityGreaduateBinding
import okhttp3.*
import java.io.IOException

class GreaduateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGreaduateBinding

    private var currentNumberOf: String = "查詢畢業審核結果"
    var numberOf = "01"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_greaduate)

        binding = ActivityGreaduateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.VISIBLE

        binding.btnBack.setOnClickListener {
            finish()
        }

        AlertDialog.Builder(this@GreaduateActivity)
            .setMessage("不好意思\n此功能遇到了某些不可描述錯誤\n如果遇到無法顯示之情況\n請多多利用右上角RESET重置\n感謝您的配合！")
            .setTitle("哎呀！")
            .setPositiveButton("我瞭解了",null )
            .show()
        getPDF()

        binding.btnReset.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            getPDF()
        }

        val numberOfData =
            listOf("查詢畢業審核結果" ,"查詢欠修學分","查詢畢業審核結果(明細)")

        val adapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, numberOfData)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                currentNumberOf = binding.spinner.selectedItem.toString()
                if (currentNumberOf == "查詢畢業審核結果") {
                    numberOf = "01"

                } else if (currentNumberOf == "查詢欠修學分") {
                    numberOf = "02"
                } else if (currentNumberOf == "查詢畢業審核結果(明細)") {
                    numberOf = "03"
                }
                binding.progressBar.visibility = View.VISIBLE
                getPDF()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

    }

    fun getPDF(){

        val token= intent.getStringExtra("token")
        val greaduateURL = "https://app.npu.edu.tw/api/getGreaduate?token=$token&numberOf=$numberOf"
        Log.v("","token :$token")

        val okHttpClient = OkHttpClient().newBuilder().build()

        //Part 2: 宣告 Request，要求要連到指定網址
        val request: Request = Request.Builder().url(greaduateURL).get().build()

        //Part 3: 宣告 Call
        val call = okHttpClient.newCall(request)

        //執行 Call 連線後，採用 enqueue 非同步方式，獲取到回應的結果資料
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("HKT", "onFailure: $e")
                binding.progressBar.visibility = View.GONE
            }

            @SuppressLint("SetJavaScriptEnabled")
            override fun onResponse(call: Call, response: Response) {

                val greaduateData = response.body?.string()

                Log.d("123", "onResponse: $greaduateData")

                val greaduateInfo = Gson().fromJson(greaduateData, GreaduateInfo::class.java)

                Log.d("QQQ", "onResponse: ${greaduateInfo.JSession}")
                Log.d("QQQ", "onResponse: ${greaduateInfo.URL}")
                Log.d("QQQ", "onResponse: ${greaduateInfo.randID}")
                Log.d("QQQ", "onResponse: ${greaduateInfo.randVal}")
                Log.d("QQQ", "onResponse: ${greaduateInfo.status}")

//                val uri = Uri.parse(greaduateInfo.URL)
                val url = greaduateInfo.URL
                val JSession = greaduateInfo.JSession
                val randID = greaduateInfo.randID
                val randVal = greaduateInfo.randVal
                val status = greaduateInfo.status
//                binding.webView.webViewClient = WebViewClient()
//                binding.webView.settings.setSupportZoom(true)
//                binding.webView.settings.javaScriptEnabled = true



                runOnUiThread {
                    var header = mutableMapOf<String, String>()
                    var str = "JSESSIONID=" + JSession + ";UqZBpD3n3mSNQRhf6Q__=" + randVal
                    header.put("Cookie",str)
                    Log.v("cookie",str)
                    Log.v("url",url)
                    binding.progressBar.visibility = View.GONE
                    binding.webView.webViewClient = WebViewClient()
                    binding.webView.settings.setSupportZoom(true)
                    binding.webView.settings.builtInZoomControls = true
                    binding.webView.settings.javaScriptEnabled = true
                    binding.webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$url",header)
                }

                //                   binding.pdfView.fromUri(Uri.parse(greaduateInfo.URL)).load()




            }
        })
    }

}