package com.example.becast.mine.ui.love

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.becast.unit.data.radioDb.RadioData
import com.example.becast.unit.data.radioDb.RadioDatabase
import java.lang.Exception
import java.util.*

class LoveViewModel(private val context:Context) {

    private var loveModel=LoveModel()
    val loveModelLiveData: MutableLiveData<LoveModel> = MutableLiveData()
    init {
        loveModelLiveData.value=loveModel
        val db = Room.databaseBuilder(context, RadioDatabase::class.java, "radio")
            .allowMainThreadQueries()
            .build()
        val mDao=db.radioDao()
        loveModel.list =mDao.getLove() as MutableList<RadioData>
        loveModelLiveData.postValue(loveModel)
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
            loveModel.list.remove(radioData)
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

        loveModelLiveData.postValue(loveModel)
    }
}