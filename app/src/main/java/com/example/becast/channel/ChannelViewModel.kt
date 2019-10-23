package com.example.becast.channel

import android.content.Context
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.becast.data.radioDb.RadioData
import com.example.becast.data.radioDb.RadioDatabase
import com.example.becast.data.rss.RssData
import com.example.becast.unit.data.rssDB.RssDatabase

class ChannelViewModel(private val context: Context, private val rssData: RssData) {

    private var flag =true
    val list : MutableList<RadioData> = mutableListOf()
    val channelLiveData: MutableLiveData<MutableList<RadioData>> = MutableLiveData()

    init {
        channelLiveData.value=list
        object : Thread(){
            override fun run() {
                super.run()
                val db = Room.databaseBuilder(context, RadioDatabase::class.java, "radio")
                    .build()
                val mDao=db.radioDao()
                list.clear()
                list.addAll(mDao.getByChannel(rssData.rssUri) as MutableList<RadioData>)
                channelLiveData.postValue(list)
                db.close()
            }
        }.start()

    }

    fun changeAll(handler: Handler):Boolean{
        flag = if(flag){
            cancelAll(handler)
        } else{
            subscribeAll(handler)
        }
        return flag
    }

    private fun cancelAll(handler: Handler):Boolean{
        object : Thread() {
            override fun run() {
                try{
                    cancelChannel()
                    cancelRadio()
                    handler.sendEmptyMessage(0x000)
                }
                catch(e:Exception){
                   // handler.sendEmptyMessage(0x001)
                }
            }
        }.start()
        return false
    }

    fun cancelChannel(){
        val db = Room.databaseBuilder(context, RssDatabase::class.java, "rss")
            .build()
        val mDao=db.rssDao()
        mDao.delete(rssData)
        db.close()
    }

    fun cancelRadio(){
        val db = Room.databaseBuilder(context, RadioDatabase::class.java, "radio")
            .build()
        val mDao=db.radioDao()
        mDao.deleteByChannel(rssData.rssUri)
        db.close()
    }

    private fun subscribeAll(handler: Handler):Boolean{
        object : Thread() {
            override fun run() {
                try{
                    subscribeRadio()
                    subscribeRss()
                    handler.sendEmptyMessage(0x001)
                }
                catch(e:Exception){
                 //   handler.sendEmptyMessage(0x001)
                }
            }
        }.start()
        return true
    }

    fun subscribeRss(){
        val db = Room.databaseBuilder(context, RssDatabase::class.java, "rss")
            .build()
        val mDao=db.rssDao()
        try{
            mDao.insert(rssData)
        }catch (e:Exception){
            if(!e.message!!.contains("SQLITE_CONSTRAINT_PRIMARYKEY")){
                throw e
            }
        }
        db.close()
    }

    fun subscribeRadio(){
        val db = Room.databaseBuilder(context, RadioDatabase::class.java, "radio")
            .build()
        val mDao=db.radioDao()
        for(item : RadioData in list){
            try{
                mDao.insert(item)
            }catch (e:Exception){
                if(e.message!!.contains("SQLITE_CONSTRAINT_PRIMARYKEY")){
                    continue
                }
                else{
                    throw e
                }
            }
        }
        db.close()
    }
}