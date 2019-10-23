package com.example.becast.home.subscribe

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.becast.data.radioDb.RadioData
import com.example.becast.data.radioDb.RadioDatabase
import java.lang.Exception
import java.util.*

class SubscribeViewModel(private val context: Context) {

    val list : MutableList<RadioData> = mutableListOf()
    val subscribeModelLiveData: MutableLiveData<MutableList<RadioData>> = MutableLiveData()

    init {
        subscribeModelLiveData.value=list

        object : Thread() {
            override fun run() {
                val db = Room.databaseBuilder(context, RadioDatabase::class.java, "radio")
                    .build()
                val mDao=db.radioDao()
                list.clear()
                list.addAll(mDao.getAll(0,50) as MutableList<RadioData>)
                subscribeModelLiveData.postValue(list)
                db.close()
            }
        }.start()
    }


    fun getMore(){
        object :Thread(){
            override fun run() {
                super.run()
                val db = Room.databaseBuilder(context, RadioDatabase::class.java, "radio")
                    .build()
                val mDao=db.radioDao()
                list.addAll(mDao.getAll(list.size,list.size+50) as MutableList<RadioData>)
                subscribeModelLiveData.postValue(list)
                db.close()
            }
        }.start()

    }
    fun update(){
        object :Thread(){
            override fun run() {
                super.run()
                list.clear()
                val db = Room.databaseBuilder(context, RadioDatabase::class.java, "radio")
                    .build()
                val mDao=db.radioDao()
                list.addAll(mDao.getAll(0,50) as MutableList<RadioData>)
                subscribeModelLiveData.postValue(list)
                db.close()
            }
        }.start()
    }
    fun changeLove(radioData: RadioData){
        if(radioData.loveTime>0){
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
    }

    fun addToRadioList(radioData: RadioData):Boolean{
        val db=Room.databaseBuilder(context, RadioDatabase::class.java,"radio")
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