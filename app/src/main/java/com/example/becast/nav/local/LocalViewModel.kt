package com.example.becast.nav.local

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.becast.data.radio.RadioData
import com.example.becast.data.radio.RadioDatabase
import com.example.becast.service.MediaHelper

class LocalViewModel {

    private val list : MutableList<RadioData> = mutableListOf()
    val localModelLiveData: MutableLiveData<MutableList<RadioData>> = MutableLiveData()
    init {
        localModelLiveData.value=list
    }

    fun getList(context: Context){
        object :Thread(){
            override fun run() {
                super.run()
                val db = RadioDatabase.getDb(context)
                val mDao=db.radioDao()
                list.clear()
                list.addAll(mDao.getLocal() as MutableList<RadioData>)
                localModelLiveData.postValue(list)
                RadioDatabase.closeDb()
            }
        }.start()
    }

    fun delRadio(context: Context,radioData: RadioData){
        val path=context.getExternalFilesDir("")
        val filename=radioData.radioUrl.hashCode()
        radioData.downloadPath=path!!.path+"/download/"+filename+".mp3"
        radioData.downloadFinish=false
        localModelLiveData.value=list
        MediaHelper().getDownload()?.downLoad(radioData)
        object :Thread(){
            override fun run() {
                super.run()
                val db = RadioDatabase.getDb(context)
                val mDao=db.radioDao()
                mDao.updateItem(radioData)
                RadioDatabase.closeDb()
            }
        }.start()
    }

}