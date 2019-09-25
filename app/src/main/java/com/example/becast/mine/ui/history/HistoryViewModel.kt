package com.example.becast.mine.ui.history

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.becast.unit.data.radioDb.RadioData
import com.example.becast.unit.data.radioDb.RadioDatabase
import java.lang.Exception
import java.util.*

class HistoryViewModel(private val context:Context) {

    private var list : MutableList<RadioData> = mutableListOf()
    val historyModelLiveData: MutableLiveData<MutableList<RadioData>> = MutableLiveData()
    init {
        historyModelLiveData.value=list
        val db = Room.databaseBuilder(context, RadioDatabase::class.java, "radio")
            .allowMainThreadQueries()
            .build()
        val mDao=db.radioDao()
        list =mDao.getHistory() as MutableList<RadioData>
        historyModelLiveData.value=list
        db.close()
    }
    fun addToRadioList(radioData: RadioData):Boolean{
        val db=Room.databaseBuilder(context,RadioDatabase::class.java,"radio")
            .allowMainThreadQueries()
            .build()
        val mDao=db.radioDao()
        try{
            mDao.insert(radioData)
        }catch(e: Exception){
            return false
        }
        db.close()
        return true
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

    fun clearList(){
        val db=Room.databaseBuilder(context,RadioDatabase::class.java,"radio")
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