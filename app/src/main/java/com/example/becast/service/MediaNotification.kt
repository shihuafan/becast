package com.example.becast.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.media.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.becast.broadcastReceiver.NotificationClickReceiver


object MediaNotification {

    fun setNotification(context:Context, xmlTitle:String, title:String, image: Bitmap, play:Boolean){
        val channelId=121321
        val notificationManager= context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {    //Android 8.0以上适配
            val channel = NotificationChannel(
                channelId.toString(),
                "channel_name",
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val mediaStyle= NotificationCompat.MediaStyle()
        mediaStyle.setShowCancelButton(true)
            .setShowActionsInCompactView(0,1,2)
        val builder = androidx.core.app.NotificationCompat.Builder(context,channelId.toString())
        val iconPlay=if(play){
            com.example.becast.R.drawable.ic_pause
        }else{
            com.example.becast.R.drawable.ic_play
        }

        val preIntent = Intent(context, NotificationClickReceiver::class.java)
        preIntent.action = "android.intent.action.RADIO_PRE"
        val intentPre =
            PendingIntent.getBroadcast(context, 0, preIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val pauseIntent = Intent(context, NotificationClickReceiver::class.java)
        pauseIntent.action = "android.intent.action.RADIO_PAUSE"
        val intentPause =
            PendingIntent.getBroadcast(context, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent = Intent(context, NotificationClickReceiver::class.java)
        nextIntent.action = "android.intent.action.RADIO_NEXT"
        val intentNext =
            PendingIntent.getBroadcast(context, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        builder.setContentTitle(xmlTitle)
            .setContentText(title)
            .setWhen(System.currentTimeMillis())
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_MAX)
            .setSmallIcon(com.example.becast.R.drawable.logo)
            .setLargeIcon(image)
            .addAction(com.example.becast.R.drawable.ic_backword,"pre",intentPre)
            .addAction(iconPlay,"pause",intentPause)
            .addAction(com.example.becast.R.drawable.ic_foeword,"next",intentNext)
            .setStyle(mediaStyle)
        if(play){
            builder.setOngoing(true)
        }
        val notification = builder.build()
        notificationManager.notify(channelId, notification)
    }

    fun setNotification(context:Context, xmlTitle:String, title:String, image: String, play:Boolean){
        Glide.with(context)
            .asBitmap()
            .load(image)
            .into(object:SimpleTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    setNotification(context,xmlTitle,title,resource,play)
                }
            })
    }

}