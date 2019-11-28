package com.example.becast.playpage.detail

import android.content.Context
import androidx.room.Room
import com.example.becast.data.radioDb.RadioData
import com.example.becast.data.radioDb.RadioDatabase
import com.example.becast.data.radioDb.RadioDatabaseHelper
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
}