package com.example.becast.service


import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import androidx.room.Room
import com.example.becast.unit.data.radioDb.RadioData
import com.example.becast.unit.data.radioDb.RadioDatabase
import java.io.IOException

class RadioService : Service() {

    private val mBinder = LocalBinder()
    private val mediaPlayer = MediaPlayer()
    private var list : MutableList<RadioData> = mutableListOf()

    internal interface MIBinder {
        fun playRadio(item: RadioData)
        fun radioItemEmpty():Boolean
        fun setRadioList(list:MutableList<RadioData>)
        fun getRadioItem(): RadioData
        fun addRadioItem(item:RadioData)
        fun getRadioList():MutableList<RadioData>
        fun playPreRadio()
        fun playNextRadio()
        fun pauseRadio():Boolean
        fun getRadioDuration():Int
        fun getRadioCurrentPosition():Int
        fun seekRadioTo( progress: Int)
        fun isRadioPlaying():Boolean
    }

    inner class LocalBinder : Binder() , MIBinder {
        override fun playRadio(item: RadioData){ play(item) }
        override fun radioItemEmpty():Boolean{return itemEmpty()}
        override fun setRadioList(list: MutableList<RadioData>){ setList(list) }
        override fun getRadioItem():RadioData{ return getItem() }
        override fun addRadioItem(item:RadioData){addItem(item)}
        override fun getRadioList():MutableList<RadioData>{ return getList() }
        override fun playPreRadio(){ playPre() }
        override fun playNextRadio(){  playNext() }
        override fun pauseRadio():Boolean{ return pause() }
        override fun getRadioDuration(): Int { return getDuration() }
        override fun getRadioCurrentPosition(): Int { return getCurrentPosition() }
        override fun seekRadioTo( progress: Int){ seekTo(progress) }
        override fun isRadioPlaying():Boolean{return isPlaying()}
    }

    override fun onBind(intent: Intent?): IBinder { return mBinder }

    override fun onCreate() {
        super.onCreate()
        val db = Room.databaseBuilder(this, RadioDatabase::class.java, "result")
            .allowMainThreadQueries()
            .build()
        val mDao=db.radioDao()
        list =mDao.getLove() as MutableList<RadioData>
        db.close()

        if(list.size>0){
            //查询待播放列表，并将最开始的加入mediaPlayer
            val url=list[0].radioUri
            try {
                mediaPlayer.setDataSource(url)
                mediaPlayer.prepareAsync()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    fun play(item: RadioData){
        //直接播放，删除当前播放，并将传入的插在列表头
        list.add(0,item)
        if(list.size>1){
            list.removeAt(1)
        }
        val url=item.radioUri
        mediaPlayer.reset()
        //重置mediaPlayer对象，防止切换时异常
        addToHistory(item,this)
        try {
            mediaPlayer.setDataSource(url)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        //异步准备，准备完成播放
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            Toast.makeText(this,"即在完成",Toast.LENGTH_SHORT).show()
            mediaPlayer.start()
        }
        mediaPlayer.setOnCompletionListener {
            if(list.size > 1){
                val temp=list[1]
                list.removeAt(1)
                play(temp)
            }
        }
    }


    fun itemEmpty():Boolean{
        return list.size<=0
    }
    fun setList(list:MutableList<RadioData>){
        this.list=list
    }

    fun getList():MutableList<RadioData>{
        return list
    }

    private fun addToHistory(item: RadioData, context: Context) {

        val db = Room.databaseBuilder(context, RadioDatabase::class.java, "result")
            .allowMainThreadQueries()
            .build()
        val mDao=db.radioDao()
        item.historyTime=System.currentTimeMillis()
        mDao.updateItem(item)
    }

    fun getItem():RadioData{
        return list[0]
    }

    fun addItem(item:RadioData){
        list.add(item)
    }

    fun getDuration(): Int {
        return mediaPlayer.duration
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

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

    fun isPlaying():Boolean{
        return mediaPlayer.isPlaying
    }

}




