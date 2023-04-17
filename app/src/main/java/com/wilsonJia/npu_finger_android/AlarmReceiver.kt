package com.wilsonJia.npu_finger_android

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import com.wilsonJia.npu_finger_android.adapter.NotifyAdapter
import com.wilsonJia.npu_finger_android.adapter.notificationId

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // 此處寄送通知
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var builder: Notification.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val channel = NotificationChannel("NPU_Finger_Course", "NPU_Finger_Course", NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(channel)
                builder = Notification.Builder(context, "NPU_Finger_Course")
            } catch (e: Exception) {
                Log.e("Jia", "Error creating notification channel: ${e.message}")
                builder = Notification.Builder(context)
            }
        } else {
            builder = Notification.Builder(context)
        }

        val pendingIntent = PendingIntent.getActivity(context, 0, Intent(context, LoginActivity::class.java), 0)

        val courseName = intent.getStringExtra("courseName")
        val courseRoom = intent.getStringExtra("courseRoom")
        Log.d("Jia","Notify")
        builder.setSmallIcon(R.drawable.ic_stat_dolphin)
            .setContentTitle(" ❗️上課提醒❗️")
            .setContentText("課程名稱：$courseName\n" +
                    "上課地點：$courseRoom")
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_finger))
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)

        notificationManager.notify(notificationId, builder.build())
    }
}