package com.example.becast.nav.love

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.becast.data.mix.MixData
import com.example.becast.data.radio.RadioData
import com.example.becast.data.radio.RadioDatabase
import com.example.becast.data.radio.RadioHttpHelper
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
                if(mixData.mixId == "0"){
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

    fun addToRadioList(radioData: RadioData){
        radioData.waitTime=Date().time
        RadioHttpHelper().updateToNet(radioData)

        object :Thread(){
            override fun run() {
                super.run()
                val db= RadioDatabase.getDb(context)
                val mDao=db.radioDao()
                mDao.updateItem(radioData)
                RadioDatabase.closeDb()
            }
        }.start()
    }

    fun changeLove(radioData: RadioData){
        if(radioData.loveTime>0){
            list.remove(radioData)
            radioData.loveTime=0
        }
        else{
            radioData.loveTime= Date().time
        }
        RadioHttpHelper().updateToNet(radioData)
        object :Thread(){
            override fun run() {
                super.run()
                val db= RadioDatabase.getDb(context)
                val mDao=db.radioDao()
                mDao.updateItem(radioData)
                RadioDatabase.closeDb()
                mixModelLiveData.postValue(list)
            }
        }.start()
    }
}