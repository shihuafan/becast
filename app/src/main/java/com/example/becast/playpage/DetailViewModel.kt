package com.example.becast.playpage

import android.content.Context
import androidx.room.Room
import com.example.becast.data.radioDb.RadioData
import com.example.becast.data.radioDb.RadioDatabase
import java.util.*

class DetailViewModel(private val context: Context,private val radioData: RadioData) {

    fun changeLove(){
        if(radioData.loveTime>0){
            radioData.loveTime=0
        }
        else{
            radioData.loveTime= Date().time
        }
        val db= Room.databaseBuilder(context, RadioDatabase::class.java,"radio")
            .allowMainThreadQueries()
            .build()
        val mDao=db.radioDao()
        mDao.updateItem(radioData)
        db.close()
    }
}