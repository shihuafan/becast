package com.example.becast.service

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import com.example.becast.broadcastReceiver.NotificationClickReceiver
import com.example.becast.service.download.RadioDownload
import com.example.becast.service.player.RadioPlayer


class RadioService : Service(){

    private val mBinder = RadioBinder()
    private lateinit var radioPlayer: RadioPlayer
    private lateinit var radioDownload: RadioDownload
    private val radioService=this
    private val receiver=NotificationClickReceiver()

    override fun onBind(intent: Intent?): RadioBinder { return mBinder }

    override fun onCreate() {
        super.onCreate()
        radioPlayer= RadioPlayer(radioService)
        radioDownload=RadioDownload(radioService)
        val intentFilterPre= IntentFilter()
        intentFilterPre.addAction("android.intent.action.RADIO_PRE")
        registerReceiver(receiver,intentFilterPre)
        val intentFilterPause= IntentFilter()
        intentFilterPause.addAction("android.intent.action.RADIO_PAUSE")
        registerReceiver(receiver,intentFilterPause)
        val intentFilterNext= IntentFilter()
        intentFilterNext.addAction("android.intent.action.RADIO_NEXT")
        registerReceiver(receiver,intentFilterNext)

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    inner class RadioBinder : Binder()  {
        fun getPlayer(): RadioPlayer {return radioPlayer}
        fun getDownload():RadioDownload{return radioDownload}
    }

}




