package com.example.becast.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import java.io.File

class DownLoadService:Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    val downLoadService=this
    override fun onCreate() {
        super.onCreate()

        val path=this.externalCacheDir
        val downLoadFromNet=DownLoadFromNet()
        val downLoadListener=DownLoadListener()
        downLoadFromNet.setListener(downLoadListener)
        path?.let {
            downLoadFromNet.execute(path.path+"/download/becast.apk")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"service结束")
    }
    inner class DownLoadListener{

        private val channelId=12
        private var notificationManager: NotificationManager =
            downLoadService.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        private var builder= NotificationCompat.Builder(downLoadService,channelId.toString())

        init {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {    //Android 8.0以上适配
                val channel = NotificationChannel(
                    channelId.toString(),
                    "channel_name",
                    NotificationManager.IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(channel)
            }

            builder.setContentTitle("下载中……")
                .setContentText("准备中……")
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(com.example.becast.R.drawable.logo)
                .setProgress(1,0,false)
                .setOngoing(true)

            notificationManager.notify(channelId, builder.build())
        }

        fun setNotification(present:Int){
            builder.setContentText("${present}%")
                .setProgress(100,present,false)
            notificationManager.notify(channelId, builder.build())
        }

        fun finish(result:String?){
            val path=result.toString()
            val notificationManager= downLoadService.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(12)
            val intent =  Intent(Intent.ACTION_VIEW)
            val file =  File(path)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (Build.VERSION.SDK_INT >= 24) {
                val apkUri = FileProvider.getUriForFile(downLoadService, "com.example.becast", file)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
            }else {
                intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive")
            }
            startActivity(intent)
            downLoadService.onDestroy()
        }
    }
}