package com.wilsonJia.npu_finger_android

import android.content.Context
import android.content.Intent
import android.hardware.biometrics.BiometricManager
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.wilsonJia.npu_finger_android.data.LoginInfo
import com.wilsonJia.npu_finger_android.databinding.ActivityLoginBinding
import okhttp3.*
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.Q)
    private val availableCodes = listOf(
        BiometricManager.BIOMETRIC_SUCCESS,
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
    )

    private lateinit var binding: ActivityLoginBinding

    var uid = ""
    var pwd = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SharedPreference(this)
        if(pref.getSwitch()){
            checkBiometric()
        }
        readData()

        binding.loginBtn.setOnClickListener{
            uid=binding.uid.text.toString()
            pwd=binding.pwd.text.toString()
            if (uid=="" || pwd ==""){
                AlertDialog.Builder(this)
                    .setMessage("你好像還沒輸入帳號或密碼呢")
                    .setTitle("哎呀！")
                    .setPositiveButton("好的",null )
                    .show()
            }else{
                if(hasNetwork()){
                    login()
                }else{
                    AlertDialog.Builder(this@LoginActivity)
                        .setMessage("你好像沒有網路連線耶！\n要不要再檢查看看呢？")
                        .setTitle("哎呀！")
                        .setPositiveButton("好的",null )
                        .show()
                }

            }

        }

        binding.guestloginBtn.setOnClickListener {
            uid="guest"
            pwd="123"
            if(hasNetwork()){
                val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
                mainIntent.putExtra("token","guest")
                startActivity(mainIntent)
            }else{
                AlertDialog.Builder(this@LoginActivity)
                    .setMessage("你好像沒有網路連線耶！\n要不要再檢查看看呢？")
                    .setTitle("哎呀！")
                    .setPositiveButton("好的",null )
                    .show()
            }


        }

    }

    private fun login() {
        val loginUrl =
            "https://app.npu.edu.tw/api/login"

        //Part 1: 宣告 OkHttpClient
        val okHttpClient = OkHttpClient().newBuilder().build()

        //加入 FormBody 參數 name 和 job 。
        val formBody: FormBody = FormBody.Builder()
            .add("uid", uid)
            .add("pwd", pwd)
            .build()

        //Part 2: 宣告 Request，要求要連到指定網址
        val request: Request = Request.Builder().url(loginUrl).post(formBody).build()

        //Part 3: 宣告 Call
        val call = okHttpClient.newCall(request)

        //執行 Call 連線後，採用 enqueue 非同步方式，獲取到回應的結果資料
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {

                val loginData = response.body?.string()

                Log.d("QQQ", "response: $loginData")

                try {
                    val loginInfo = Gson().fromJson(loginData, LoginInfo::class.java)
                    // handle the response
                    if (loginInfo.error=="帳號密碼錯誤"){
                        Looper.prepare()
                        AlertDialog.Builder(this@LoginActivity)
                            .setMessage("帳號或密碼錯誤\n請你再檢查看看")
                            .setTitle("哎呀！")
                            .setPositiveButton("好的",null )
                            .show()
                        Looper.loop()
                    }else{
                        saveData()
                        binding.pwd.setText("")
                        val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
                        mainIntent.putExtra("token",loginInfo.token)
                        mainIntent.putExtra("uid",uid)
                        mainIntent.putExtra("pwd",pwd)
                        startActivity(mainIntent)
                    }

                } catch (e: JsonSyntaxException) {
                    e.printStackTrace()
                    Looper.prepare()
                    AlertDialog.Builder(this@LoginActivity)
                        .setMessage("出現了一點錯誤！\n請嘗試重新登入")
                        .setTitle("哎呀！")
                        .setPositiveButton("好的",null )
                        .show()
                    Looper.loop()
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                    Looper.prepare()
                    AlertDialog.Builder(this@LoginActivity)
                        .setMessage("出現了一點錯誤！\n請嘗試重新登入")
                        .setTitle("哎呀！")
                        .setPositiveButton("好的",null )
                        .show()
                    Looper.loop()
                }


            }
        })}

    private fun readData() {
        val pref = SharedPreference(this)
        binding.uid.setText(pref.getUID())

    }

    private fun saveData() {
        val pref = SharedPreference(this)
        pref.saveUser(binding.uid.text.toString(),binding.pwd.text.toString())

    }

    private fun fingerLogin() {
        val pref = SharedPreference(this)
        val loginUrl =
            "https://app.npu.edu.tw/api/login"

        //Part 1: 宣告 OkHttpClient
        val okHttpClient = OkHttpClient().newBuilder().build()

        //加入 FormBody 參數 name 和 job 。
        val formBody: FormBody = FormBody.Builder()
            .add("uid", pref.getUID().toString())
            .add("pwd", pref.getPWD().toString())
            .build()

        //Part 2: 宣告 Request，要求要連到指定網址
        val request: Request = Request.Builder().url(loginUrl).post(formBody).build()

        //Part 3: 宣告 Call
        val call = okHttpClient.newCall(request)

        //執行 Call 連線後，採用 enqueue 非同步方式，獲取到回應的結果資料
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {

                val loginData = response.body?.string()

                Log.d("QQQ", "response: $loginData")

//                val loginInfo = Gson().fromJson(loginData, LoginInfo::class.java)
                try {
                    val loginInfo = Gson().fromJson(loginData, LoginInfo::class.java)
                    // handle the response
                    if (loginInfo.error=="帳號密碼錯誤"){
                        Looper.prepare()
                        AlertDialog.Builder(this@LoginActivity)
                            .setMessage("帳號或密碼錯誤\n請你再檢查看看")
                            .setTitle("哎呀！")
                            .setPositiveButton("好的",null )
                            .show()
                        Looper.loop()

                    }else{
                        saveData()
                        binding.pwd.setText("")
                        val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
                        mainIntent.putExtra("token",loginInfo.token)
                        mainIntent.putExtra("uid",uid)
                        mainIntent.putExtra("pwd",pwd)
                        startActivity(mainIntent)
                    }

                } catch (e: JsonSyntaxException) {
                    e.printStackTrace()
                    Looper.prepare()
                    AlertDialog.Builder(this@LoginActivity)
                        .setMessage("出現了一點錯誤！\n請嘗試重新登入")
                        .setTitle("哎呀！")
                        .setPositiveButton("好的",null )
                        .show()
                    Looper.loop()
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                    Looper.prepare()
                    AlertDialog.Builder(this@LoginActivity)
                        .setMessage("出現了一點錯誤！\n請嘗試重新登入")
                        .setTitle("哎呀！")
                        .setPositiveButton("好的",null )
                        .show()
                    Looper.loop()
                }


            }
        })}
    private fun checkBiometric() {
        canAuthenticateWithBiometrics()?.let {
            if (it) {
                showBiometricPrompt()
            } else {
                Toast.makeText(this, getString(R.string.cannot_use_biometric), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun canAuthenticateWithBiometrics(): Boolean? {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val fingerprintManagerCompat = FingerprintManagerCompat.from(this)
            fingerprintManagerCompat.hasEnrolledFingerprints() && fingerprintManagerCompat.isHardwareDetected
        } else {
            val biometricManager = this.getSystemService(BiometricManager::class.java)
            biometricManager?.let {
                return availableCodes.contains(biometricManager.canAuthenticate())
            } ?: false
        }
    }

    private fun showBiometricPrompt() {
        val authenticationCallback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(
                    applicationContext,
                    getString(R.string.fingerprint_success),
                    Toast.LENGTH_SHORT
                ).show()
//                readBData()
                if (hasNetwork()){
                    fingerLogin()
                }else{
                    AlertDialog.Builder(this@LoginActivity)
                        .setMessage("你好像沒有網路連線耶！\n要不要再檢查看看呢？")
                        .setTitle("哎呀！")
                        .setPositiveButton("好的",null )
                        .show()
                }

            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(
                    applicationContext,
                    errString.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(
                    applicationContext,
                    getString(R.string.fingerprint_fail),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        val mBiometricPrompt = BiometricPrompt(this, mainExecutor, authenticationCallback)
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setDescription(getString(R.string.plz_input_ur_fingerprint))
            .setTitle(getString(R.string.fingerprint))
            .setNegativeButtonText(getString(R.string.cancel))
            .build()
        mBiometricPrompt.authenticate(promptInfo)
    }

    private fun hasNetwork(): Boolean {
        var connectivityManager: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            return network != null // 如果是null代表没有網路
        }
        return false
    }
}