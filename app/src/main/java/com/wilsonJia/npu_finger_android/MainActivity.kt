package com.wilsonJia.npu_finger_android

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.wilsonJia.npu_finger_android.Util.OkHttpUtil
import com.wilsonJia.npu_finger_android.Util.OkHttpUtil.Companion.mOkHttpUtil
import com.wilsonJia.npu_finger_android.adapter.NewsAdapter
import com.wilsonJia.npu_finger_android.data.NewsInfo
import com.wilsonJia.npu_finger_android.data.NewsInfoItem
import com.wilsonJia.npu_finger_android.data.UserInfo
import com.wilsonJia.npu_finger_android.data.weatherData.WeatherInfo
import com.wilsonJia.npu_finger_android.databinding.ActivityMainBinding
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() , NewsAdapter.IItemClickListener{
    private lateinit var viewAdapter: NewsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var binding: ActivityMainBinding

    private var phoneCallPermissionGranted = false

    private val phoneNumber = "0928483123"

    private val REQUEST_PHONE_CALL = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)

        toolbar.setOnMenuItemClickListener {
            when(it.itemId){

                R.id.menu_setting -> {
                    val token= intent.getStringExtra("token")
                    if(token=="guest"){
                        AlertDialog.Builder(this)
                            .setMessage("你還沒登入沒辦法做設定唷！")
                            .setTitle("哎呀❗")
                            .setPositiveButton("好的",null )
                            .show()
                    }else {
                        val intent = Intent(this@MainActivity, SettingActivity::class.java)
                        startActivity(intent)
                    }


                }

                R.id.menu_iot -> {
                    val intent = Intent(this@MainActivity, IotActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_house -> {
                    if (hasNetwork()){
                        val intent = Intent(this@MainActivity, HouseActivity::class.java)
                        startActivity(intent)
                    }else{
                        noNetwork()
                    }

                }
                R.id.menu_restaurant -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com.tw/maps/search/%E9%A4%90%E5%BB%B0/@23.575495,119.580949,15z/data=!4m2!2m1!6e5?hl=zh-TW"))
                    startActivity(intent)
                }
                R.id.menu_hospital -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com.tw/maps/search/%E9%86%AB%E9%99%A2/@23.575495,119.580949,15z?hl=zh-TW"))
                    startActivity(intent)
                }

            }
            false
        }

        initView()
        getNews()
        getUserInfo()
        getWeather()

        val pref = SharedPreference(this)
        if(pref.getWeatherSwitch()){
            binding.tvRain.isVisible=true
            binding.weather.isVisible=true
            binding.tvTemp.isVisible=true
            binding.tvWeather.isVisible=true
            binding.user.isVisible=false
        }else{
            binding.tvRain.isVisible=false
            binding.weather.isVisible=false
            binding.tvTemp.isVisible=false
            binding.tvWeather.isVisible=false
            binding.user.isVisible=true
        }

        binding.user.setOnClickListener {

        }
        binding.user.setOnLongClickListener {
            AlertDialog.Builder(this)
                .setMessage("作者還寫不出來換頭貼的功能喔")
                .setTitle("哎呀❗")
                .setPositiveButton("好的",null )
                .show()
            true
        }

        binding.btnCalendar.setOnClickListener {
            if(hasNetwork()){
                val calendarIntent = Intent(this@MainActivity, CalendarActivity::class.java)
                startActivity(calendarIntent)
            }else{
                noNetwork()
            }

        }

        binding.btnScore.setOnClickListener {
            val token= intent.getStringExtra("token")
            if(token=="guest"){
                AlertDialog.Builder(this)
                    .setMessage("你還沒登入想做甚麼？")
                    .setTitle("哎呀❗")
                    .setPositiveButton("好的",null )
                    .show()
            }else{
                if(hasNetwork()) {
                    val scoreIntent = Intent(this@MainActivity, ScoreActivity::class.java)
                    scoreIntent.putExtra("token",token)
                    startActivity(scoreIntent)
                }else{
                    noNetwork()
                }

            }

        }

        binding.btnNoshow.setOnClickListener {
            val token= intent.getStringExtra("token")
            val uid = intent.getStringExtra("uid")
            val pwd = intent.getStringExtra("pwd")
            if(token=="guest"){
                AlertDialog.Builder(this)
                    .setMessage("你還沒登入想做甚麼？")
                    .setTitle("哎呀❗")
                    .setPositiveButton("好的",null )
                    .show()
            }else{
                if (hasNetwork()){
                    val noShowIntent = Intent(this@MainActivity, NoShowActivity::class.java)
                    noShowIntent.putExtra("token",token)
                    noShowIntent.putExtra("uid",uid)
                    noShowIntent.putExtra("pwd",pwd)
                    startActivity(noShowIntent)
                }else{
                    noNetwork()
                }

            }

        }

        binding.btnReward.setOnClickListener {
            val token= intent.getStringExtra("token")
            if(token=="guest"){
                AlertDialog.Builder(this)
                    .setMessage("你還沒登入想做甚麼？")
                    .setTitle("哎呀❗")
                    .setPositiveButton("好的",null )
                    .show()
            }else{
                if (hasNetwork()){
                    val rewardIntent = Intent(this@MainActivity, RewardActivity::class.java)
                    rewardIntent.putExtra("token",token)
                    startActivity(rewardIntent)
                }else{
                    noNetwork()
                }

            }

        }


        binding.btnCoursetable.setOnClickListener {
            val token= intent.getStringExtra("token")
            if(token=="guest"){
                AlertDialog.Builder(this)
                    .setMessage("你還沒登入想做甚麼？")
                    .setTitle("哎呀❗")
                    .setPositiveButton("好的",null )
                    .show()
            }else{
                if(hasNetwork()){
                    val courseTableIntent = Intent(this@MainActivity, CourseTableActivity::class.java)
                    courseTableIntent.putExtra("token",token)
                    startActivity(courseTableIntent)
                }else{
                    noNetwork()
                }

            }

        }

        binding.btnCall.setOnClickListener {

            AlertDialog.Builder(this)
                .setMessage("確定要撥打給校安中心嗎？")
                .setTitle("❗❗")
                .setPositiveButton("確定", DialogInterface.OnClickListener{ _, _ ->
                    startCall()
                })
                .setNeutralButton("不要", null)
                .show()
        }

        binding.btnGreaduate.setOnClickListener {
            val token= intent.getStringExtra("token")
            if(token=="guest"){
                AlertDialog.Builder(this)
                    .setMessage("你還沒登入想做甚麼？")
                    .setTitle("哎呀❗")
                    .setPositiveButton("好的",null )
                    .show()
            }else{
                if (hasNetwork()){
                    val GreaduateIntent = Intent(this@MainActivity, GreaduateActivity::class.java)
                    GreaduateIntent.putExtra("token",token)
                    startActivity(GreaduateIntent)
                }else{
                    noNetwork()
                }

            }

        }

        binding.btnLogout.setOnClickListener {
            finish()
        }


    }


    private fun startCall() {

        getPhoneCallPermission()

    }

    private fun getWeather(){
        val newsURL = "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-D0047-045?Authorization=CWB-7C98545C-C9D5-43D1-8D6C-0F1160D41608&limit=1&offset=0&format=JSON&locationName=%E9%A6%AC%E5%85%AC%E5%B8%82&elementName=PoP12h&elementName=T,Wx"
        OkHttpUtil.mOkHttpUtil.getAsync(newsURL, object : OkHttpUtil.ICallback{

            override fun onFailure(e: IOException) {

            }

            override fun onResponse(response: Response) {

                val weatherData = response?.body?.string()
                Log.d("QQQ", "response: $weatherData")

                val weatherInfo = Gson().fromJson(weatherData, WeatherInfo::class.java)


                runOnUiThread {
                    binding.tvWeather.text=weatherInfo.records.locations[0].location[0].weatherElement[1].time[0].elementValue[0].value
                    if(binding.tvWeather.text=="晴"){
                        binding.tvWeather.text=weatherInfo.records.locations[0].location[0].weatherElement[1].time[0].elementValue[0].value+"天"
                        binding.weather.setImageResource(R.drawable.sunny)
                    }
                    if(binding.tvWeather.text=="多雲"){
                        binding.tvWeather.text=weatherInfo.records.locations[0].location[0].weatherElement[1].time[0].elementValue[0].value
                        binding.weather.setImageResource(R.drawable.partlycloudy)
                    }
                    if(binding.tvWeather.text=="陰"){
                        binding.tvWeather.text=weatherInfo.records.locations[0].location[0].weatherElement[1].time[0].elementValue[0].value+"天"
                        binding.weather.setImageResource(R.drawable.cloudy)
                    }
                    if(binding.tvWeather.text=="短暫陣雨或雷雨"||binding.tvWeather.text=="陣雨或雷雨"){
                        binding.tvWeather.text=weatherInfo.records.locations[0].location[0].weatherElement[1].time[0].elementValue[0].value
                        binding.weather.setImageResource(R.drawable.rainy)
                    }
                    if(binding.tvWeather.text=="短暫陣雨"||binding.tvWeather.text=="陣雨"||binding.tvWeather.text=="短暫雨"){
                        binding.tvWeather.text=weatherInfo.records.locations[0].location[0].weatherElement[1].time[0].elementValue[0].value
                        binding.weather.setImageResource(R.drawable.shortriany)
                    }

                    binding.tvRain.text="降雨機率 "+weatherInfo.records.locations[0].location[0].weatherElement[0].time[0].elementValue[0].value+"％"
                    binding.tvTemp.text="平均溫度 "+weatherInfo.records.locations[0].location[0].weatherElement[2].time[0].elementValue[0].value+"度"

                }
            }

        })
    }


    //學校網站掛掉會出錯
    private fun getNews(){
        val newsURL = "https://app.npu.edu.tw/api/newsList"
        OkHttpUtil.mOkHttpUtil.getAsync(newsURL, object : OkHttpUtil.ICallback{

            override fun onFailure(e: IOException) {
                binding.progressBar.visibility = View.GONE
            }

            override fun onResponse(response: Response) {

//                try {
//                    val newsData = response?.body?.string()
//
//                    val newsInfo = Gson().fromJson(newsData, NewsInfo::class.java)
//                }catch (exception: java.lang.IllegalStateException) {
//                    runOnUiThread {
//                        binding.progressBar.visibility = View.GONE
//                        binding.tvError.text="ERROR!!"
//                    }
//
//                }finally {
//                    runOnUiThread {
//                        binding.progressBar.visibility = View.GONE
//                        viewAdapter.newsList = newsInfo
//                    }
//                }


                val newsData = response?.body?.string()

                val newsInfo = Gson().fromJson(newsData, NewsInfo::class.java)

                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    viewAdapter.newsList = newsInfo
                }
            }
        })
    }

    private fun initView() {
        //
        binding.progressBar.visibility = View.VISIBLE

        // 定義 LayoutManager 為 LinearLayoutManager
        viewManager = LinearLayoutManager(this)

        // 自定義 Adapte 為 MainAdapter，稍後再定義 MainAdapter 這個類別
        viewAdapter = NewsAdapter(this)

        // 定義從佈局當中，拿到 recycler_view 元件，
        binding.recyclerView.apply {
            // 透過 kotlin 的 apply 語法糖，設定 LayoutManager 和 Adapter
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }


    private fun getUserInfo() {
        val token = intent.getStringExtra("token")


        if (token == "guest") {
            runOnUiThread {
                binding.tvUid.text = "0000000000"
                binding.tvHi.text = "嗨！訪客模式"
                binding.tvClass.text = "尚未登入"
            }
        } else {
            val USER_URL = "https://app.npu.edu.tw/api/info?token=$token"
            mOkHttpUtil.getAsync(USER_URL, object : OkHttpUtil.ICallback {
                override fun onFailure(e: IOException) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(response: Response) {
                    val userData: String? = response.body?.string()

                    val userInfo = Gson().fromJson(userData, UserInfo::class.java)

                    runOnUiThread {
                        binding.tvUid.text = userInfo.stdid
                        binding.tvHi.text = "嗨！" + userInfo.name
                        binding.tvClass.text = userInfo.myClass
                    }


                }

            })
        }
    }

    private fun getPhoneCallPermission() {
        //檢查權限
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //已獲取到權限
            phoneCallPermissionGranted = true
            val callIntent = Intent(Intent.ACTION_CALL)
            Toast.makeText(this, "正在撥打給校安中心", Toast.LENGTH_LONG).show()
            callIntent.setData(Uri.parse("tel:"+phoneNumber))

            startActivity(callIntent)
        } else {
            //詢問要求獲取權限
            requestPhoneCallPermission()
        }
    }

    private fun requestPhoneCallPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.CALL_PHONE
            )
        ) {
            AlertDialog.Builder(this)
                .setMessage("需要撥號權限才可以撥打電話給校安喔")
                .setPositiveButton("確定") { _, _ ->
                    ActivityCompat.requestPermissions(
                        this, arrayOf(Manifest.permission.CALL_PHONE),
                        REQUEST_PHONE_CALL
                    )
                }
                .setNegativeButton("取消",null)
                .show()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_PHONE_CALL -> {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //已獲取到權限
                        phoneCallPermissionGranted = true
                        //todo checkPhoneCallState()
                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.CALL_PHONE
                            )
                        ) {
                            //權限被永久拒絕
                            Toast.makeText(this, "撥號權限已被關閉，功能將會無法正常使用", Toast.LENGTH_SHORT).show()

                            AlertDialog.Builder(this)
                                .setTitle("開啟撥號權限")
                                .setMessage("此應用程式，撥號權限已被關閉，需開啟才能正常使用")
                                .setPositiveButton("確定") { _, _ ->
                                    openPermissionSettings()

                                }
                                .setNegativeButton("取消",null)
                                .show()
                        } else {
                            //權限被拒絕
                            Toast.makeText(this, "撥號權限被拒絕，功能將會無法正常使用", Toast.LENGTH_SHORT).show()
                            requestPhoneCallPermission()
                        }
                    }
                }
            }
        }
    }

    private fun openPermissionSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_PHONE_CALL -> {
                getPhoneCallPermission()
            }

        }
    }

    private fun hasNetwork(): Boolean {
        var connectivityManager: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            return network != null // 如果是null代表没有网络
        }
        return false
    }

    private fun noNetwork(){
        AlertDialog.Builder(this@MainActivity)
            .setMessage("你好像沒有網路連線耶！\n要不要再檢查看看呢？")
            .setTitle("哎呀❗")
            .setPositiveButton("好的",null )
            .show()
    }

    override fun onItemClickListener(data: NewsInfoItem) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.newsURL))
        startActivity(intent)
    }
}