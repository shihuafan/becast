package com.example.becast.nav.history

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.becast.data.radio.RadioData
import com.example.becast.data.radio.RadioDatabase
import com.example.becast.data.radio.RadioHttpHelper

class HistoryViewModel {

    private val list : MutableList<RadioData> = mutableListOf()
    val historyModelLiveData: MutableLiveData<MutableList<RadioData>> = MutableLiveData()
    init {
        historyModelLiveData.value=list
    }

    fun getList(context:Context){
        object :Thread(){
            override fun run() {
                super.run()
                val db = RadioDatabase.getDb(context)
                val mDao=db.radioDao()
                list.clear()
                list.addAll(mDao.getHistory() as MutableList<RadioData>)
                historyModelLiveData.postValue(list)
                RadioDatabase.closeDb()
            }
        }.start()
    }

    fun clearList(context:Context){
        RadioHttpHelper().clearHistory()
        object :Thread(){
            override fun run() {
                super.run()
                val db= RadioDatabase.getDb(context)
                val mDao=db.radioDao()
                for(item in list){
                    item.historyTime=0
                    mDao.updateItem(item)
                }
                RadioDatabase.closeDb()
                list.clear()
                historyModelLiveData.postValue(list)
            }
        }.start()



    }
}