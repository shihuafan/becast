package com.example.becast.service

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import androidx.room.Room
import com.example.becast.data.radioDb.RadioData
import com.example.becast.data.radioDb.RadioDatabase
import java.io.IOException
import java.util.*
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class RadioService : Service() {


    private val mBinder = LocalBinder()
    private val mediaPlayer = MediaPlayer()
    private val list : MutableList<RadioData> = mutableListOf()
    private lateinit var context: Context
    private val timer=Timer()
    private val task=object : TimerTask() {
        override fun run() {
            updateProgress()
        }
    }
    @Volatile
    private var isPrepared=false

    internal interface MIBinder {
        fun playRadio(item: RadioData)
        fun radioItemEmpty():Boolean
        fun setRadioList(list:MutableList<RadioData>)
        fun getRadioItem(): RadioData
        fun addRadioItem(item: RadioData)
        fun getRadioList():MutableList<RadioData>
        fun playPreRadio()
        fun playNextRadio()
        fun pauseRadio():Boolean
        fun getRadioDuration():Int
        fun getRadioCurrentPosition():Int
        fun seekRadioTo( progress: Int)
        fun isRadioPlaying():Boolean
        fun isPrepared():Boolean
    }

    inner class LocalBinder : Binder() , MIBinder {
        override fun playRadio(item: RadioData){ play(item) }
        override fun radioItemEmpty():Boolean{return itemEmpty()}
        override fun setRadioList(list: MutableList<RadioData>){ setList(list) }
        override fun getRadioItem(): RadioData { return getItem() }
        override fun addRadioItem(item: RadioData){addItem(item)}
        override fun getRadioList():MutableList<RadioData>{ return getList() }
        override fun playPreRadio(){ playPre() }
        override fun playNextRadio(){  playNext() }
        override fun pauseRadio():Boolean{ return pause() }
        override fun getRadioDuration(): Int { return getDuration() }
        override fun getRadioCurrentPosition(): Int { return getCurrentPosition() }
        override fun seekRadioTo( progress: Int){ seekTo(progress) }
        override fun isRadioPlaying():Boolean{return isPlaying()}
        override fun isPrepared():Boolean{return isPrepared}
    }

    override fun onBind(intent: Intent?): IBinder { return mBinder }

    override fun onCreate() {
        super.onCreate()
        context=this
        val handler=Handler{
            playList()
            false
        }
        object:Thread(){
            override fun run() {
                super.run()
                val db = Room.databaseBuilder(context, RadioDatabase::class.java, "radio")
                    .build()
                val mDao=db.radioDao()
                list.clear()
                list.addAll(mDao.getWait(0,50) as MutableList<RadioData>)
                db.close()
                handler.sendEmptyMessage(0x000)
            }
        }.start()




        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            isPrepared=true
            try {
                timer.schedule(task,0,10000)
            }catch (e:Exception){}
        }
        mediaPlayer.setOnCompletionListener {
            timer.cancel()
            clearProgress(list[0])
            if(list.size <= 1){
                seekTo(0)
                mediaPlayer.start()
            }
            else{
                list.removeAt(0)
                playList()
            }
        }

    }


    fun play(item: RadioData){
        timer.cancel()
        //正在播放则直接进入
        if(list.size>0 && list[0]==item){
            return
        }
        else if (list.size > 0){
            list.remove(item)
            list.removeAt(0)
        }
        list.add(0,item)
        addToWait(item)

        playList()
    }

    private fun playList(){
        if(list.size>0){
            isPrepared=false
            mediaPlayer.reset()
            //重置mediaPlayer对象，防止切换时异常
            addToHistory(list[0])
            try {
                mediaPlayer.setDataSource(list[0].radioUri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            mediaPlayer.prepareAsync()
        }
    }


    fun itemEmpty()=(list.size<=0)

    fun setList(list:MutableList<RadioData>){
        this.list.clear()
        this.list.addAll(list)
    }

    fun getList()=list

    private fun addToHistory(item: RadioData) {
//        val db = Room.databaseBuilder(this, RadioDatabase::class.java, "radio")
//            .allowMainThreadQueries()
//            .build()
//        val mDao=db.radioDao()
//        item.historyTime=System.currentTimeMillis()
//        mDao.updateItem(item)
    }

    fun getItem()=list[0]

    fun addItem(item: RadioData){
        list.add(item)
    }

    fun getDuration()=mediaPlayer.duration

    fun getCurrentPosition()=mediaPlayer.currentPosition

    fun playPre(){
        var time=getCurrentPosition()-15*1000
        if(time <= 0){
            time = 0
        }
        mediaPlayer.seekTo(time)
    }

    fun playNext(){
        var time=getCurrentPosition()+30*1000
        if(time >= getDuration()){
            time = getDuration()
        }
        mediaPlayer.seekTo(time)
    }

    fun pause():Boolean{
        return if(mediaPlayer.isPlaying){
            mediaPlayer.pause()
            false
        } else{
            mediaPlayer.start()
            true
        }
    }

    fun seekTo( progress: Int){
        mediaPlayer.seekTo(progress)
    }

    fun changeplayerSpeed(speed: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mediaPlayer.isPlaying) {

                mediaPlayer.playbackParams = mediaPlayer.playbackParams.setSpeed(speed)
            } else {
                mediaPlayer.playbackParams = mediaPlayer.playbackParams.setSpeed(speed)
                mediaPlayer.pause()
            }
        }
    }

    fun isPlaying()=mediaPlayer.isPlaying

    private fun updateProgress(){
//        if(list.size<=0){
//            return
//        }
//        list[0].progress=mediaPlayer.currentPosition
//        object :Thread(){
//            override fun run() {
//                super.run()
//                val db=Room.databaseBuilder(context, RadioDatabase::class.java,"radio")
//                    .build()
//                val mDao=db.radioDao()
//                mDao.updateItem(list[0])
//                db.close()
//            }
//        }.start()
    }

    private fun clearProgress(item: RadioData){
        item.progress=0
        item.waitTime=0
        object :Thread(){
            override fun run() {
                super.run()
                val db=Room.databaseBuilder(context, RadioDatabase::class.java,"radio")
                    .build()
                val mDao=db.radioDao()
                mDao.updateItem(item)
                db.close()
            }
        }.start()
    }

    private fun addToWait(item: RadioData){
        item.waitTime=System.currentTimeMillis()
        object :Thread(){
            override fun run() {
                super.run()
                val db=Room.databaseBuilder(context, RadioDatabase::class.java,"radio")
                    .build()
                val mDao=db.radioDao()
                mDao.updateItem(item)
                db.close()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}




