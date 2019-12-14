package com.example.becast.channel

import android.content.Context
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.becast.data.radio.RadioData
import com.example.becast.data.radio.RadioDatabase
import com.example.becast.data.radio.RadioDatabaseHelper
import com.example.becast.data.xml.XmlData
import com.example.becast.data.xml.XmlDatabase

class ChannelViewModel(private val context: Context, private val xmlData: XmlData) {

    private var flag =true
    val list : MutableList<RadioData> = mutableListOf()
    val channelLiveData: MutableLiveData<MutableList<RadioData>> = MutableLiveData()

    init {
        channelLiveData.value=list
        object : Thread(){
            override fun run() {
                super.run()
                val db = RadioDatabaseHelper.getDb(context)
                val mDao=db.radioDao()
                list.clear()
                list.addAll(mDao.getByChannel(xmlData.xmlUrl) as MutableList<RadioData>)
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
        val db = Room.databaseBuilder(context, XmlDatabase::class.java, "rss")
            .build()
        val mDao=db.xmlDao()
        mDao.delete(xmlData)
        db.close()
    }

    fun cancelRadio(){
        val db = Room.databaseBuilder(context, RadioDatabase::class.java, "radio")
            .build()
        val mDao=db.radioDao()
        mDao.deleteByChannel(xmlData.xmlUrl)
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
        val db = Room.databaseBuilder(context, XmlDatabase::class.java, "rss")
            .build()
        val mDao=db.xmlDao()
        try{
            mDao.insert(xmlData)
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