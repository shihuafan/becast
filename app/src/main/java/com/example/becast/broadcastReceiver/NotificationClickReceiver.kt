package com.example.becast.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.becast.service.MediaHelper

class NotificationClickReceiver : BroadcastReceiver() {

    val ACTION_PAUSE = "android.intent.action.RADIO_PAUSE"
    val ACTION_PRE = "android.intent.action.RADIO_PRE"
    val ACTION_NEXT = "android.intent.action.RADIO_NEXT"

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG,intent.toString())
        val binder= MediaHelper().getPlayer()
        when(intent?.action){
            ACTION_PAUSE->{
                binder?.pauseRadio()
            }
            ACTION_PRE->{
                binder?.playPreRadio()
            }
            ACTION_NEXT->{
                binder?.playNextRadio()
            }
        }

    }
}