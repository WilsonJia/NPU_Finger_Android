package com.wilsonJia.npu_finger_android


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.wilsonJia.npu_finger_android.Util.OkHttpUtil
import com.wilsonJia.npu_finger_android.data.courseTableData.CourseTableInfo
import com.wilsonJia.npu_finger_android.databinding.ActivityCourseTableBinding
import okhttp3.Response
import java.io.IOException

private var teacher: String = ""
private var room: String = ""
private var courseName: String = ""


lateinit var courseTableInfo: CourseTableInfo

class CourseTableActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCourseTableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_table)

        binding = ActivityCourseTableBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnNotify.setOnClickListener {
            val token= intent.getStringExtra("token")
            val notifyIntent = Intent(this@CourseTableActivity, CourseNotifyActivity::class.java)
            notifyIntent.putExtra("token",token)
            startActivity(notifyIntent)
        }

        getCourseTable()



    }

    private fun getCourseTable(){
        val token= intent.getStringExtra("token")
        var TIME_TABLE_URL = "https://app.npu.edu.tw/api/coursetable?token=$token"
        OkHttpUtil.mOkHttpUtil.getAsync(TIME_TABLE_URL, object : OkHttpUtil.ICallback{

            override fun onFailure(e: IOException) {
                binding.progressBar.visibility = View.GONE
            }

            override fun onResponse(response: Response) {

                val courseTableData = response?.body?.string()

                Log.d("QQQ", "response: $courseTableData")

                courseTableInfo = Gson().fromJson(courseTableData, CourseTableInfo::class.java)



                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    setCourseNamesToTextViews()
                    setCourseOnClickListener()


                }


            }

        })
    }

    fun formatCourseName(courseName: String): String =
        courseName.replace("*", "")
            .substringBefore("-")

    fun setCourseNamesToTextViews() {
        val courseTextViewMap = mapOf(
            binding.tv11 to courseTableInfo.value[0].No1.Monday.courseName,
            binding.tv12 to courseTableInfo.value[1].No2.Monday.courseName,
            binding.tv13 to courseTableInfo.value[2].No3.Monday.courseName,
            binding.tv14 to courseTableInfo.value[3].No4.Monday.courseName,
            binding.tv15 to courseTableInfo.value[4].No5.Monday.courseName,
            binding.tv16 to courseTableInfo.value[5].No6.Monday.courseName,
            binding.tv17 to courseTableInfo.value[6].No7.Monday.courseName,
            binding.tv18 to courseTableInfo.value[7].No8.Monday.courseName,
            binding.tv21 to courseTableInfo.value[0].No1.Tuesday.courseName,
            binding.tv22 to courseTableInfo.value[1].No2.Tuesday.courseName,
            binding.tv23 to courseTableInfo.value[2].No3.Tuesday.courseName,
            binding.tv24 to courseTableInfo.value[3].No4.Tuesday.courseName,
            binding.tv25 to courseTableInfo.value[4].No5.Tuesday.courseName,
            binding.tv26 to courseTableInfo.value[5].No6.Tuesday.courseName,
            binding.tv27 to courseTableInfo.value[6].No7.Tuesday.courseName,
            binding.tv28 to courseTableInfo.value[7].No8.Tuesday.courseName,
            binding.tv31 to courseTableInfo.value[0].No1.Wednesday.courseName,
            binding.tv32 to courseTableInfo.value[1].No2.Wednesday.courseName,
            binding.tv33 to courseTableInfo.value[2].No3.Wednesday.courseName,
            binding.tv34 to courseTableInfo.value[3].No4.Wednesday.courseName,
            binding.tv35 to courseTableInfo.value[4].No5.Wednesday.courseName,
            binding.tv36 to courseTableInfo.value[5].No6.Wednesday.courseName,
            binding.tv37 to courseTableInfo.value[6].No7.Wednesday.courseName,
            binding.tv38 to courseTableInfo.value[7].No8.Wednesday.courseName,
            binding.tv41 to courseTableInfo.value[0].No1.Thursday.courseName,
            binding.tv42 to courseTableInfo.value[1].No2.Thursday.courseName,
            binding.tv43 to courseTableInfo.value[2].No3.Thursday.courseName,
            binding.tv44 to courseTableInfo.value[3].No4.Thursday.courseName,
            binding.tv45 to courseTableInfo.value[4].No5.Thursday.courseName,
            binding.tv46 to courseTableInfo.value[5].No6.Thursday.courseName,
            binding.tv47 to courseTableInfo.value[6].No7.Thursday.courseName,
            binding.tv48 to courseTableInfo.value[7].No8.Thursday.courseName,
            binding.tv51 to courseTableInfo.value[0].No1.Friday.courseName,
            binding.tv52 to courseTableInfo.value[1].No2.Friday.courseName,
            binding.tv53 to courseTableInfo.value[2].No3.Friday.courseName,
            binding.tv54 to courseTableInfo.value[3].No4.Friday.courseName,
            binding.tv55 to courseTableInfo.value[4].No5.Friday.courseName,
            binding.tv56 to courseTableInfo.value[5].No6.Friday.courseName,
            binding.tv57 to courseTableInfo.value[6].No7.Friday.courseName,
            binding.tv58 to courseTableInfo.value[7].No8.Friday.courseName
        )
        courseTextViewMap.forEach { (textView, courseName) ->
            textView.text = formatCourseName(courseName)
        }
    }
    fun setCourseOnClickListener() {
        binding.tv11.setOnClickListener {
            teacher=courseTableInfo.value[0].No1.Monday.teacher
            room=courseTableInfo.value[0].No1.Monday.room
            courseName=courseTableInfo.value[0].No1.Monday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv12.setOnClickListener {
            teacher=courseTableInfo.value[1].No2.Monday.teacher
            room=courseTableInfo.value[1].No2.Monday.room
            courseName=courseTableInfo.value[1].No2.Monday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv13.setOnClickListener {
            teacher=courseTableInfo.value[2].No3.Monday.teacher
            room=courseTableInfo.value[2].No3.Monday.room
            courseName=courseTableInfo.value[2].No3.Monday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv14.setOnClickListener {
            teacher=courseTableInfo.value[3].No4.Monday.teacher
            room=courseTableInfo.value[3].No4.Monday.room
            courseName=courseTableInfo.value[3].No4.Monday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv15.setOnClickListener {
            teacher=courseTableInfo.value[4].No5.Monday.teacher
            room=courseTableInfo.value[4].No5.Monday.room
            courseName=courseTableInfo.value[4].No5.Monday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv16.setOnClickListener {
            teacher=courseTableInfo.value[5].No6.Monday.teacher
            room=courseTableInfo.value[5].No6.Monday.room
            courseName=courseTableInfo.value[5].No6.Monday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv17.setOnClickListener {
            teacher=courseTableInfo.value[6].No7.Monday.teacher
            room=courseTableInfo.value[6].No7.Monday.room
            courseName=courseTableInfo.value[6].No7.Monday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv18.setOnClickListener {
            teacher=courseTableInfo.value[7].No8.Monday.teacher
            room=courseTableInfo.value[7].No8.Monday.room
            courseName=courseTableInfo.value[7].No8.Monday.courseName.replace("*","")
            getAlertDialog()
        }
        //Tuesday

        binding.tv21.setOnClickListener {
            teacher=courseTableInfo.value[0].No1.Tuesday.teacher
            room=courseTableInfo.value[0].No1.Tuesday.room
            courseName=courseTableInfo.value[0].No1.Tuesday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv22.setOnClickListener {
            teacher=courseTableInfo.value[1].No2.Tuesday.teacher
            room=courseTableInfo.value[1].No2.Tuesday.room
            courseName=courseTableInfo.value[1].No2.Tuesday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv23.setOnClickListener {
            teacher=courseTableInfo.value[2].No3.Tuesday.teacher
            room=courseTableInfo.value[2].No3.Tuesday.room
            courseName=courseTableInfo.value[2].No3.Tuesday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv24.setOnClickListener {
            teacher=courseTableInfo.value[3].No4.Tuesday.teacher
            room=courseTableInfo.value[3].No4.Tuesday.room
            courseName=courseTableInfo.value[3].No4.Tuesday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv25.setOnClickListener {
            teacher=courseTableInfo.value[4].No5.Tuesday.teacher
            room=courseTableInfo.value[4].No5.Tuesday.room
            courseName=courseTableInfo.value[4].No5.Tuesday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv26.setOnClickListener {
            teacher=courseTableInfo.value[5].No6.Tuesday.teacher
            room=courseTableInfo.value[5].No6.Tuesday.room
            courseName=courseTableInfo.value[5].No6.Tuesday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv27.setOnClickListener {
            teacher=courseTableInfo.value[6].No7.Tuesday.teacher
            room=courseTableInfo.value[6].No7.Tuesday.room
            courseName=courseTableInfo.value[6].No7.Tuesday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv28.setOnClickListener {
            teacher=courseTableInfo.value[7].No8.Tuesday.teacher
            room=courseTableInfo.value[7].No8.Tuesday.room
            courseName=courseTableInfo.value[7].No8.Tuesday.courseName.replace("*","")
            getAlertDialog()
        }
        //Wednesday

        binding.tv31.setOnClickListener {
            teacher=courseTableInfo.value[0].No1.Wednesday.teacher
            room=courseTableInfo.value[0].No1.Wednesday.room
            courseName=courseTableInfo.value[0].No1.Wednesday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv32.setOnClickListener {
            teacher=courseTableInfo.value[1].No2.Wednesday.teacher
            room=courseTableInfo.value[1].No2.Wednesday.room
            courseName=courseTableInfo.value[1].No2.Wednesday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv33.setOnClickListener {
            teacher=courseTableInfo.value[2].No3.Wednesday.teacher
            room=courseTableInfo.value[2].No3.Wednesday.room
            courseName=courseTableInfo.value[2].No3.Wednesday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv34.setOnClickListener {
            teacher=courseTableInfo.value[3].No4.Wednesday.teacher
            room=courseTableInfo.value[3].No4.Wednesday.room
            courseName=courseTableInfo.value[3].No4.Wednesday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv35.setOnClickListener {
            teacher=courseTableInfo.value[4].No5.Wednesday.teacher
            room=courseTableInfo.value[4].No5.Wednesday.room
            courseName=courseTableInfo.value[4].No5.Wednesday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv36.setOnClickListener {
            teacher=courseTableInfo.value[5].No6.Wednesday.teacher
            room=courseTableInfo.value[5].No6.Wednesday.room
            courseName=courseTableInfo.value[5].No6.Wednesday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv37.setOnClickListener {
            teacher=courseTableInfo.value[6].No7.Wednesday.teacher
            room=courseTableInfo.value[6].No7.Wednesday.room
            courseName=courseTableInfo.value[6].No7.Wednesday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv38.setOnClickListener {
            teacher=courseTableInfo.value[7].No8.Wednesday.teacher
            room=courseTableInfo.value[7].No8.Wednesday.room
            courseName=courseTableInfo.value[7].No8.Wednesday.courseName.replace("*","")
            getAlertDialog()
        }
        //Thursday

        binding.tv41.setOnClickListener {
            teacher=courseTableInfo.value[0].No1.Thursday.teacher
            room=courseTableInfo.value[0].No1.Thursday.room
            courseName=courseTableInfo.value[0].No1.Thursday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv42.setOnClickListener {
            teacher=courseTableInfo.value[1].No2.Thursday.teacher
            room=courseTableInfo.value[1].No2.Thursday.room
            courseName=courseTableInfo.value[1].No2.Thursday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv43.setOnClickListener {
            teacher=courseTableInfo.value[2].No3.Thursday.teacher
            room=courseTableInfo.value[2].No3.Thursday.room
            courseName=courseTableInfo.value[2].No3.Thursday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv44.setOnClickListener {
            teacher=courseTableInfo.value[3].No4.Thursday.teacher
            room=courseTableInfo.value[3].No4.Thursday.room
            courseName=courseTableInfo.value[3].No4.Thursday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv45.setOnClickListener {
            teacher=courseTableInfo.value[4].No5.Thursday.teacher
            room=courseTableInfo.value[4].No5.Thursday.room
            courseName=courseTableInfo.value[4].No5.Thursday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv46.setOnClickListener {
            teacher=courseTableInfo.value[5].No6.Thursday.teacher
            room=courseTableInfo.value[5].No6.Thursday.room
            courseName=courseTableInfo.value[5].No6.Thursday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv47.setOnClickListener {
            teacher=courseTableInfo.value[6].No7.Thursday.teacher
            room=courseTableInfo.value[6].No7.Thursday.room
            courseName=courseTableInfo.value[6].No7.Thursday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv48.setOnClickListener {
            teacher=courseTableInfo.value[7].No8.Thursday.teacher
            room=courseTableInfo.value[7].No8.Thursday.room
            courseName=courseTableInfo.value[7].No8.Thursday.courseName.replace("*","")
            getAlertDialog()
        }
        //Friday

        binding.tv51.setOnClickListener {
            teacher=courseTableInfo.value[0].No1.Friday.teacher
            room=courseTableInfo.value[0].No1.Friday.room
            courseName=courseTableInfo.value[0].No1.Friday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv52.setOnClickListener {
            teacher=courseTableInfo.value[1].No2.Friday.teacher
            room=courseTableInfo.value[1].No2.Friday.room
            courseName=courseTableInfo.value[1].No2.Friday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv53.setOnClickListener {
            teacher=courseTableInfo.value[2].No3.Friday.teacher
            room=courseTableInfo.value[2].No3.Friday.room
            courseName=courseTableInfo.value[2].No3.Friday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv54.setOnClickListener {
            teacher=courseTableInfo.value[3].No4.Friday.teacher
            room=courseTableInfo.value[3].No4.Friday.room
            courseName=courseTableInfo.value[3].No4.Friday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv55.setOnClickListener {
            teacher=courseTableInfo.value[4].No5.Friday.teacher
            room=courseTableInfo.value[4].No5.Friday.room
            courseName=courseTableInfo.value[4].No5.Friday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv56.setOnClickListener {
            teacher=courseTableInfo.value[5].No6.Friday.teacher
            room=courseTableInfo.value[5].No6.Friday.room
            courseName=courseTableInfo.value[5].No6.Friday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv57.setOnClickListener {
            teacher=courseTableInfo.value[6].No7.Friday.teacher
            room=courseTableInfo.value[6].No7.Friday.room
            courseName=courseTableInfo.value[6].No7.Friday.courseName.replace("*","")
            getAlertDialog()
        }

        binding.tv58.setOnClickListener {
            teacher=courseTableInfo.value[7].No8.Friday.teacher
            room=courseTableInfo.value[7].No8.Friday.room
            courseName=courseTableInfo.value[7].No8.Friday.courseName.replace("*","")
            getAlertDialog()
        }

    }


    private fun getAlertDialog(){
        if(courseName!=" "){
            AlertDialog.Builder(this@CourseTableActivity)
                .setMessage("\uD83D\uDC68\u200D\uD83C\uDFEB授課老師：$teacher\n\uD83C\uDFEB上課教室：$room")
                .setTitle("$courseName")
                .setPositiveButton("好的",null )
                .show()
        }

    }
}