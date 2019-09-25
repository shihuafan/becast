package com.example.becast.mine.ui.subscribe

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.becast.unit.data.radioDb.RadioData
import com.example.becast.unit.data.radioDb.RadioDatabase
import java.lang.Exception
import java.util.*

class SubscribeViewModel(private val context: Context) {

    var list : MutableList<RadioData> = mutableListOf()
    val subscribeModelLiveData: MutableLiveData<MutableList<RadioData>> = MutableLiveData()

    init {
        subscribeModelLiveData.value=list
        val db = Room.databaseBuilder(context, RadioDatabase::class.java, "radio")
            .allowMainThreadQueries()
            .build()
        val mDao=db.radioDao()
        list =mDao.getAll() as MutableList<RadioData>
        subscribeModelLiveData.value=list
        db.close()
//        object : Thread() {
//            override fun run() {
//
//            }
//        }.start()
    }

    fun changeLove(radioData:RadioData){
        if(radioData.loveTime>0){
            radioData.loveTime=0
        }
        else{
            radioData.loveTime= Date().time
        }
        val db=Room.databaseBuilder(context,RadioDatabase::class.java,"radio")
            .allowMainThreadQueries()
            .build()
        val mDao=db.radioDao()
        mDao.updateItem(radioData)
        db.close()
    }

    fun addToRadioList(radioData: RadioData):Boolean{
        val db=Room.databaseBuilder(context,RadioDatabase::class.java,"radio")
            .allowMainThreadQueries()
            .build()
        val mDao=db.radioDao()
        try{
            mDao.insert(radioData)
        }catch(e:Exception){
            return false
        }
        db.close()
        return true
    }
}