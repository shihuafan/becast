package com.example.becast.service

import android.app.Service
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.becast.broadcastReceiver.MBroadcastReceiver
import com.example.becast.broadcastReceiver.NotificationClickReceiver
import com.example.becast.data.radio.RadioData
import com.example.becast.data.radio.RadioDatabase
import java.io.IOException
import java.util.*


class RadioService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener{

    private val mBinder = MediaBinder()
    private val mediaPlayer = MediaPlayer()
    private val list : MutableList<RadioData> = mutableListOf()
    val listLiveData: MutableLiveData<MutableList<RadioData>> = MutableLiveData()
    private lateinit var context: Context
    private var firstFlag=true
    private var timer=Timer()
    private val task=object : TimerTask() {
        override fun run() {
            updateProgress()
        }
    }
    @Volatile
    private var isPrepared=false
    private val receiver=NotificationClickReceiver()

    override fun onBind(intent: Intent?): MediaIBinder { return mBinder }

    override fun onCreate() {
        super.onCreate()
        context=this
        listLiveData.value=list

        val intentFilterPre= IntentFilter()
        intentFilterPre.addAction("android.intent.action.RADIO_PRE")
        registerReceiver(receiver,intentFilterPre)
        val intentFilterPause= IntentFilter()
        intentFilterPause.addAction("android.intent.action.RADIO_PAUSE")
        registerReceiver(receiver,intentFilterPause)
        val intentFilterNext= IntentFilter()
        intentFilterNext.addAction("android.intent.action.RADIO_NEXT")
        registerReceiver(receiver,intentFilterNext)

        val handler=Handler{
            playList()
            false
        }

        object:Thread(){
            override fun run() {
                super.run()
                val db = RadioDatabase.getDb(context)
                val mDao=db.radioDao()
                list.clear()
                list.addAll(mDao.getWait(0,50) as MutableList<RadioData>)
                listLiveData.postValue(list)
                RadioDatabase.closeDb()
                if(list.size>0){
                    handler.sendEmptyMessage(0x000)
                }else{
                    firstFlag=false
                }
            }
        }.start()

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    override fun onPrepared(mp: MediaPlayer?) {
        list[0].historyTime=System.currentTimeMillis()
        list[0].duration=mediaPlayer.duration
        updateItem(list[0])
        //调用待播放列表并预加载完成
        if(firstFlag){
            firstFlag=false
            MediaNotification.setNotification(context,list[0].xmlTitle,list[0].title,list[0].xmlImageUrl,false)
        }
        else{
            mp?.start()
            MediaNotification.setNotification(context,list[0].xmlTitle,list[0].title,list[0].xmlImageUrl,true)
        }
        isPrepared=true

        //开始TimerTask
        try {
            timer.schedule(task,0,1000)
        }catch (e:Exception){}
    }

    override fun onCompletion(mp: MediaPlayer?) {
        //重置TimerTask
        timer.cancel()
        timer=Timer()
        list[0].waitTime=0
        updateItem(list[0])
        if(list.size <= 1){
            mediaPlayer.seekTo(0)
            mediaPlayer.start()
        }
        else{
            list.removeAt(0)
            listLiveData.value=list
            playList()
        }
    }

/*
    1、取消TimerTask的更新进度
    2、判断是否是当前播放，是则不处理
    3、判断是否在播放列表，在则替换到第一个
    4、不在则删除第一个后加入到第一个
    5、更新数据库中的存储时间（加入待播放列表的时间用替掉的item）
 */
    fun play(item: RadioData){
        //正在播放则直接进入
        if(list.size>0 && list[0]==item){
            return
        }
        else if (list.size > 0 && list.contains(item)){
            list.remove(item)
        }
        else if(list.size>0){
            list.removeAt(0)
        }
        timer.cancel()
        timer=Timer()
        item.waitTime=System.currentTimeMillis()
        list.add(0,item)
        listLiveData.value=list
        updateItem(item)
        playList()
    }
    /*
        播放列表，调用该函数播放第一个节目
    */
    private fun playList(){
        if(list.size>0){
            isPrepared=false
            try {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(list[0].radioUrl)
                mediaPlayer.setOnPreparedListener(this)
                mediaPlayer.setOnCompletionListener(this)
                mediaPlayer.prepareAsync()
                Log.d(TAG,"准备"+list[0].title)
                Log.d(TAG,list[0].radioUrl)

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    fun addItem(item: RadioData){
        item.waitTime=System.currentTimeMillis()
        list.add(item)
        listLiveData.value=list
        updateItem(item)
    }

    fun addItemToNext(item: RadioData){
        item.waitTime=System.currentTimeMillis()
        if(list.size>1){
            list.add(1,item)
        }
        else{
            list.add(item)
        }
        listLiveData.value=list
        updateItem(item)
    }

    fun deleteItem(index: Int){
        val item=list.removeAt(index)
        listLiveData.value=list
        item.waitTime=0
        updateItem(item)
        if(index==0){
            playList()
        }
    }

    fun playPre(){
        var time=mediaPlayer.currentPosition-15*1000
        if(time <= 0){
            time = 0
        }
        mediaPlayer.seekTo(time)
    }

    fun playNext(){
        var time=mediaPlayer.currentPosition+30*1000
        if(time >= mediaPlayer.duration){
            time = mediaPlayer.duration
        }
        mediaPlayer.seekTo(time)
    }

    fun pause():Boolean{
        val play= if(mediaPlayer.isPlaying){
            mediaPlayer.pause()
            false
        } else{
            mediaPlayer.start()
            true
        }
        MediaNotification.setNotification(context,list[0].xmlTitle,list[0].title,list[0].xmlImageUrl,play)
        return play
    }

    fun changeSpeed(speed: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.playbackParams = mediaPlayer.playbackParams.setSpeed(speed)
            } else {
                mediaPlayer.playbackParams = mediaPlayer.playbackParams.setSpeed(speed)
                mediaPlayer.pause()
            }
        }
    }

    private fun updateProgress(){
        if(list.size<=0 || !mediaPlayer.isPlaying){
            return
        }
        list[0].progress=mediaPlayer.currentPosition
        updateItem(list[0])
    }

    @Synchronized
    private fun updateItem(item: RadioData){
        object :Thread(){
            override fun run() {
                super.run()
                val db= RadioDatabase.getDb(context)
                val mDao=db.radioDao()
                mDao.updateItem(item)
                RadioDatabase.closeDb()
            }
        }.start()
    }

    inner class MediaBinder : Binder() , MediaIBinder {
        override fun playPreRadio(){ playPre() }
        override fun playNextRadio(){  playNext() }
        override fun pauseRadio():Boolean{ return pause() }
        override fun getRadioDuration(): Int { return mediaPlayer.duration }
        override fun getRadioCurrentPosition(): Int { return mediaPlayer.currentPosition }
        override fun seekRadioTo( progress: Int){ mediaPlayer.seekTo(progress) }
        override fun isRadioPlaying():Boolean{return mediaPlayer.isPlaying }
        override fun isPrepared():Boolean{return isPrepared}
        override fun changeRadioSpeed(speed: Float){ changeSpeed(speed)}

        override fun playRadio(item: RadioData){ play(item) }
        override fun addRadioItem(item: RadioData){ addItem(item) }
        override fun addRadioItemToNext(item: RadioData){ addItemToNext(item) }
        override fun deleteRadioItem(index:Int){deleteItem(index)}


        override fun radioItemEmpty():Boolean{ return list.size<=0 }
        override fun getRadioItem(): RadioData { return list[0] }
        override fun getLiveData(): MutableLiveData<MutableList<RadioData>>{return listLiveData}

    }

}




