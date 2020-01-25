package com.example.becast.channel

import android.content.Context
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.becast.data.UserData
import com.example.becast.data.radio.RadioData
import com.example.becast.data.radio.RadioDatabase
import com.example.becast.data.radio.RadioHttpHelper
import com.example.becast.data.xml.XmlData
import com.example.becast.data.xml.XmlDatabase
import com.example.becast.data.xml.XmlHttpHelper

class ChannelViewModel(private val context: Context, private val xmlData: XmlData) {

    private var flag =true
    val list : MutableList<RadioData> = mutableListOf()
    val channelLiveData: MutableLiveData<MutableList<RadioData>> = MutableLiveData()

    init {
        channelLiveData.value=list
        getAll()

    }

    fun getAll(){
        object : Thread(){
            override fun run() {
                super.run()
                val db = RadioDatabase.getDb(context)
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
        object:Thread(){
            override fun run() {
                super.run()
                try{
                    val xmlDb = XmlDatabase.getDb(context)
                    val xmlDao=xmlDb.xmlDao()
                    xmlDao.delete(xmlData)
                    XmlDatabase.closeDb()
                    XmlHttpHelper().delList(UserData.uid.toString(),xmlData.xmlUrl)

                    val radioDb = RadioDatabase.getDb(context)
                    val radioDao=radioDb.radioDao()
                    radioDao.deleteByChannel(xmlData.xmlUrl)
                    RadioDatabase.closeDb()
                    RadioHttpHelper().delList(UserData.uid.toString(),xmlData.xmlUrl)
                    handler.sendEmptyMessage(0x000)
                }catch (e:Exception){

                }
            }
        }.start()
        return false
    }

    private fun subscribeAll(handler: Handler):Boolean{
        object : Thread() {
            override fun run() {
                super.run()
                try {
                    val xmlDb=XmlDatabase.getDb(context)
                    val xmlDao=xmlDb.xmlDao()
                    xmlDao.insert(xmlData)
                    XmlDatabase.closeDb()

                    val radioDb=RadioDatabase.getDb(context)
                    val radioDao=radioDb.radioDao()
                    radioDao.insertAll(list)
                    RadioDatabase.closeDb()

                    handler.sendEmptyMessage(0x001)
                }catch (e:Exception){

                }
            }
        }.start()
        return true
    }
}