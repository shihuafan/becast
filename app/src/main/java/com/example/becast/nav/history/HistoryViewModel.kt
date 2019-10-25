package com.example.becast.nav.history

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.becast.data.radioDb.RadioData
import com.example.becast.data.radioDb.RadioDatabase
import java.lang.Exception
import java.util.*

class HistoryViewModel(private val context:Context) {

    private val list : MutableList<RadioData> = mutableListOf()
    val historyModelLiveData: MutableLiveData<MutableList<RadioData>> = MutableLiveData()
    init {
        historyModelLiveData.value=list
    }

    fun getList(){
        object :Thread(){
            override fun run() {
                super.run()
                val db = Room.databaseBuilder(context, RadioDatabase::class.java, "radio")
                    .allowMainThreadQueries()
                    .build()
                val mDao=db.radioDao()
                list.clear()
                list.addAll(mDao.getHistory() as MutableList<RadioData>)
                historyModelLiveData.postValue(list)
                db.close()
            }
        }.start()
    }

    fun clearList(){
        val db=Room.databaseBuilder(context, RadioDatabase::class.java,"radio")
            .allowMainThreadQueries()
            .build()
        val mDao=db.radioDao()
        for(item in list){
            item.historyTime=0
            mDao.updateItem(item)
        }
        db.close()
        list.clear()
        historyModelLiveData.postValue(list)


    }
}