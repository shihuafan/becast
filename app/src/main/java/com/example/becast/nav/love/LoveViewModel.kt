package com.example.becast.nav.love

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.becast.data.radioDb.RadioData
import com.example.becast.data.radioDb.RadioDatabase
import java.lang.Exception
import java.util.*

class LoveViewModel(private val context:Context) {

    private var list : MutableList<RadioData> = mutableListOf()
    val loveModelLiveData: MutableLiveData<MutableList<RadioData>> = MutableLiveData()

    init {
        loveModelLiveData.value=list
        val db = Room.databaseBuilder(context, RadioDatabase::class.java, "radio")
            .allowMainThreadQueries()
            .build()
        val mDao=db.radioDao()
        list =mDao.getLove() as MutableList<RadioData>
        loveModelLiveData.value=list
        db.close()
    }

    fun addToRadioList(radioData: RadioData):Boolean{
        radioData.waitTime=Date().time
        val db=Room.databaseBuilder(context, RadioDatabase::class.java,"radio")
            .allowMainThreadQueries()
            .build()
        val mDao=db.radioDao()
        try{
            mDao.updateItem(radioData)
            db.close()
        }catch(e: Exception){
            return false
        }
        return true
    }

    fun changeLove(radioData: RadioData){
        if(radioData.loveTime>0){
            list.remove(radioData)
            radioData.loveTime=0
        }
        else{
            radioData.loveTime= Date().time
        }
        val db=Room.databaseBuilder(context, RadioDatabase::class.java,"radio")
            .allowMainThreadQueries()
            .build()
        val mDao=db.radioDao()
        mDao.updateItem(radioData)
        db.close()

        loveModelLiveData.postValue(list)
    }
}