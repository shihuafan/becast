package com.example.becast.nav.love

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.becast.data.mix.MixData
import com.example.becast.data.radio.RadioData
import com.example.becast.data.radio.RadioDatabase
import java.lang.Exception
import java.util.*

class MixViewModel(private val mixData: MixData,private val context:Context) {

    private val list : MutableList<RadioData> = mutableListOf()
    val mixModelLiveData: MutableLiveData<MutableList<RadioData>> = MutableLiveData()

    init {
        mixModelLiveData.value=list
        object : Thread(){
            override fun run() {
                super.run()
                val db = RadioDatabase.getDb(context)
                val mDao=db.radioDao()
                if(mixData.time==1L){
                    list.addAll(mDao.getLove() as MutableList<RadioData>)
                }
                else{
                    list.addAll(mDao.getByMix(mixData.time) as MutableList<RadioData>)
                }
                mixModelLiveData.postValue(list)
                RadioDatabase.closeDb()
            }
        }.start()

    }

    fun addToRadioList(radioData: RadioData):Boolean{
        radioData.waitTime=Date().time
        val db= RadioDatabase.getDb(context)
        val mDao=db.radioDao()
        try{
            mDao.updateItem(radioData)
            RadioDatabase.closeDb()
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
        val db= RadioDatabase.getDb(context)
        val mDao=db.radioDao()
        mDao.updateItem(radioData)
        RadioDatabase.closeDb()
        mixModelLiveData.postValue(list)
    }
}