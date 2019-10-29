package com.example.becast.service


import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import androidx.room.Room
import com.example.becast.data.radioDb.RadioData
import com.example.becast.data.radioDb.RadioDatabase
import java.io.IOException
import java.util.*

class RadioService : Service() {

    private val mBinder = LocalBinder()
    private val mediaPlayer = MediaPlayer()
    private lateinit var radioData:RadioData
    private val list : MutableList<RadioData> = mutableListOf()
    private lateinit var context: Context
    @Volatile
    private var isPrepared=true

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
        object:Thread(){
            override fun run() {
                super.run()
                val db = Room.databaseBuilder(context, RadioDatabase::class.java, "radio")
                    .build()
                val mDao=db.radioDao()
                list.clear()
                list.addAll(mDao.getWait(0,50) as MutableList<RadioData>)
                db.close()

                if(list.size>0){
                    //查询待播放列表，并将最开始的加入mediaPlayer
                    radioData=list[0]
                    val url=list[0].radioUri
                    try {
                        mediaPlayer.setDataSource(url)
                        isPrepared=false
                        mediaPlayer.prepareAsync()
                        mediaPlayer.setOnPreparedListener {
                            isPrepared=true
                            mediaPlayer.seekTo(list[0].progress)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()



        Timer().schedule(object : TimerTask() { override fun run() { updateProgress() }}, 0, 10000)

    }


    fun play(item: RadioData){
        //直接播放，删除当前播放，并将传入的插在列表头
        list.add(0,item)
        val url=item.radioUri
        mediaPlayer.reset()
        //重置mediaPlayer对象，防止切换时异常
        addToHistory(item)
        try {
            mediaPlayer.setDataSource(url)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        //异步准备，准备完成播放
        isPrepared=false
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            isPrepared=true
        }
        mediaPlayer.setOnCompletionListener {

//            Toast.makeText(context,"finish",Toast.LENGTH_SHORT).show()
//            if(list.size > 0){
//                list.removeAt(0)
//                if(list.size> 0){
//                    play(list.removeAt(0))
//                }
//            }
        }
    }



    fun itemEmpty()=(list.size<=0)

    fun setList(list:MutableList<RadioData>){
        this.list.clear()
        this.list.addAll(list)
    }

    fun getList()=list

    private fun addToHistory(item: RadioData) {
        val db = Room.databaseBuilder(this, RadioDatabase::class.java, "radio")
            .allowMainThreadQueries()
            .build()
        val mDao=db.radioDao()
        item.historyTime=System.currentTimeMillis()
        mDao.updateItem(item)
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

    fun isPlaying()=mediaPlayer.isPlaying

    private fun updateProgress(){
        if(list.size<=0){
            return
        }
        list[0].progress=mediaPlayer.currentPosition
        object :Thread(){
            override fun run() {
                super.run()
                val db=Room.databaseBuilder(context, RadioDatabase::class.java,"radio")
                    .build()
                val mDao=db.radioDao()
                mDao.updateItem(list[0])
                db.close()
            }
        }.start()
    }

}




