package com.example.becast.playpage.detail

import android.content.Context
import androidx.room.Room
import com.example.becast.data.radio.RadioData
import com.example.becast.data.radio.RadioDatabaseHelper
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
                val db= RadioDatabaseHelper.getDb(context)
                val mDao=db.radioDao()
                mDao.updateItem(radioData)
                RadioDatabaseHelper.closeDb()
            }
        }.start()

    }

    fun getRssData(url:String):XmlData{
        val db = Room.databaseBuilder<XmlDatabase>(context, XmlDatabase::class.java, "rss")
            .allowMainThreadQueries()
            .build()
        val mDao=db.xmlDao()
        val rssData=mDao.getRssData(url)
        db.close()
        return rssData
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