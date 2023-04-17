package com.wilsonJia.npu_finger_android

import android.content.Context

class SharedPreference(context: Context) {
    private val pref = context.getSharedPreferences("Setting", Context.MODE_PRIVATE)

//    儲存登入
    fun saveUser(UID: String,PWD: String) {
        pref.edit()
            .putString("UID", UID)
            .putString("PWD", PWD)
            .apply()
    }

    fun getUID() : String? {
        return pref.getString("UID","")
    }
    fun getPWD() : String? {
        return pref.getString("PWD","")
    }

    fun saveSwitch(SWITCH: Boolean){
        pref.edit()
            .putBoolean("SWITCH", SWITCH)
            .apply()
    }

    fun saveWeatherSwitch(SWITCH: Boolean){
        pref.edit()
            .putBoolean("WSWITCH", SWITCH)
            .apply()
    }

    fun saveUpdateSwitch(SWITCH: Boolean){
        pref.edit()
            .putBoolean("USWITCH", SWITCH)
            .apply()
    }

    fun getSwitch(): Boolean{
        return pref.getBoolean("SWITCH",false)
    }

    fun getUpdateSwitch(): Boolean{
        return pref.getBoolean("USWITCH",false)
    }
    fun getWeatherSwitch(): Boolean{
        return pref.getBoolean("WSWITCH",false)
    }


}