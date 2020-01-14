package com.example.becast.playpage.detail

import android.content.Context
import androidx.room.Room
import com.example.becast.data.radio.RadioData
import com.example.becast.data.radio.RadioDatabase
import com.example.becast.data.radio.RadioHttpHelper
import com.example.becast.data.xml.XmlData
import com.example.becast.data.xml.XmlDatabase
import java.text.SimpleDateFormat
import java.util.*

class DetailViewModel(private val context: Context,private val radioData: RadioData) {

    fun changeLove(){
        if(radioData.loveTime>0){
            radioData.loveTime=0
        }
        else{
            radioData.loveTime= System.currentTimeMillis()
        }
        object:Thread(){
            override fun run() {
                super.run()
                val db= RadioDatabase.getDb(context)
                val mDao=db.radioDao()
                mDao.updateItem(radioData)
                RadioDatabase.closeDb()
                RadioHttpHelper().updateToNet(radioData)
            }
        }.start()
    }

    fun getXmlData(url:String):XmlData{
        val db = XmlDatabase.getDb(context)
        val mDao=db.xmlDao()
        val xmlData=mDao.getXmlData(url)
        db.close()
        return xmlData
    }

    fun getDateString(update:Long):String{
        val date= Date(update)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        return try {
            sdf.format(date)
        } catch (e: Exception) {
            ""
        }
    }
}