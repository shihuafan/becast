package com.example.becast.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyBroadcastReceiver : BroadcastReceiver() {
    private val ACTION_BOOT = "com.example.broadcasttest.MY_BROADCAST"
    override fun onReceive(context: Context, intent: Intent) {
        if (ACTION_BOOT == intent.action)
            Toast.makeText(context, "收到告白啦~", Toast.LENGTH_SHORT).show()
    }
}