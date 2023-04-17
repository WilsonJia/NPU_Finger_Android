package com.wilsonJia.npu_finger_android

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.wilsonJia.npu_finger_android.databinding.ActivitySettingBinding

private lateinit var binding: ActivitySettingBinding

var clickTime=0

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)



        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSwitch()



        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.fingerprintSwitch.setOnClickListener {
            if (binding.fingerprintSwitch.isChecked){
                val pref = SharedPreference(this)
                pref.saveSwitch(true)
                Log.d("QQQ", "response: true")
            }else{
                val pref = SharedPreference(this)
                pref.saveSwitch(false)
                Log.d("QQQ", "response: false")
            }


        }

        binding.updateSwitch.setOnClickListener {
            if (binding.updateSwitch.isChecked){
                val pref = SharedPreference(this)
                pref.saveUpdateSwitch(true)
                Log.d("QQQ", "response: true")
            }else{
                val pref = SharedPreference(this)
                pref.saveUpdateSwitch(false)
                Log.d("QQQ", "response: false")
            }


        }

        binding.weatherSwitch.setOnClickListener {
            if (binding.weatherSwitch.isChecked){
                val pref = SharedPreference(this)
                pref.saveWeatherSwitch(true)
                Log.d("QQQ", "response: true")
            }else{
                val pref = SharedPreference(this)
                pref.saveWeatherSwitch(false)
                Log.d("QQQ", "response: false")
            }

            AlertDialog.Builder(this@SettingActivity)
                .setMessage("將於下次登入時改變")
                .setTitle("天氣設定更新成功")
                .setPositiveButton("OK",null )
                .show()

        }
        binding.instagram.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/"))
            startActivity(intent)
        }
        binding.github.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/WilsonJia/NPU_Finger_Android"))
            startActivity(intent)
        }
        binding.feedback.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLScXY9XFi0XMUEWc7XexJFosEtFcdP3Uu_nYt0EHw_Q2SW1fyQ/viewform"))
            startActivity(intent)
        }

        binding.tvNPUFinger.setOnClickListener {
            clickTime++
            if(clickTime==7){
                AlertDialog.Builder(this@SettingActivity)
                    .setMessage("可以不要再點我了嗎？\n這裡沒有藏東西喔！")
                    .setTitle("哎呀❗")
                    .setPositiveButton("好的",null )
                    .show()
                clickTime=0
            }
        }

    }

    private fun getSwitch(){
        val pref = SharedPreference(this)

        binding.fingerprintSwitch.isChecked = pref.getSwitch()
        binding.updateSwitch.isChecked=pref.getUpdateSwitch()
        binding.weatherSwitch.isChecked=pref.getWeatherSwitch()
        val test = pref.getSwitch()
        Log.d("QQQ", "$test")
    }

//

}