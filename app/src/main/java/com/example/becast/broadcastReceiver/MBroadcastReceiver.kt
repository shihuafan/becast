package com.example.becast.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import org.greenrobot.eventbus.EventBus


class MBroadcastReceiver : BroadcastReceiver() {
    private val ACTION_BOOT = "android.intent.action.BOOT_COMPLETED"
    private val HEADSET_PLUG="android.intent.action.HEADSET_PLUG"

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action){
            HEADSET_PLUG->{
                if (intent.hasExtra("state")) {
                    if (0 == intent.getIntExtra("state", 0)) {
                        Log.d(TAG,"耳机已拔出")
                        EventBus.getDefault().post("stop_radio")
                    } else if (1 == intent.getIntExtra("state", 0)) {
                        Log.d(TAG,"耳机已插入")
                    }
                }
            }
        }


    }
}