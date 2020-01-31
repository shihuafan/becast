package com.example.becast.service

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.example.becast.service.download.RadioDownload
import com.example.becast.service.player.RadioPlayer

class MediaHelper {

    companion object{
        private var mIBinder: RadioService.RadioBinder ?= null
        private val conn=MyConnection()

        class MyConnection:ServiceConnection{
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                mIBinder = service as RadioService.RadioBinder
            }

            override fun onServiceDisconnected(name: ComponentName?) {}
        }
    }

    fun getBinder(activity: AppCompatActivity? = null):RadioService.RadioBinder?{
        mIBinder?.let {
            return it
        }
        activity?.let {
            val intent = Intent(it, RadioService::class.java)
            it.bindService(intent,conn, AppCompatActivity.BIND_AUTO_CREATE)
        }
        return mIBinder
    }

    fun unbindService(activity: AppCompatActivity){
        mIBinder?.let {
            activity.unbindService(conn)
            activity.stopService(Intent(activity,RadioService::class.java))
        }
    }

    fun getPlayer(): RadioPlayer?{
        mIBinder?.let {
            return it.getPlayer()
        }
        return null
    }

    fun getDownload(): RadioDownload?{
        mIBinder?.let {
            return it.getDownload()
        }
        return null
    }

}