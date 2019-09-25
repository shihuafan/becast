package com.example.becast.mine.page

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.becast.data.radioList.RadioListData
import com.example.becast.data.radioList.RadioListDatabase
import java.util.*

class PageViewModel(private val context: Context) {
    var list: MutableList<RadioListData> = mutableListOf()
    val pageModelLiveData: MutableLiveData<MutableList<RadioListData>> = MutableLiveData()

    init{
        pageModelLiveData.value=list
        val db=Room.databaseBuilder(context,RadioListDatabase::class.java,"radio_list")
            .allowMainThreadQueries()
            .build()
        val mDao=db.radioListDao()
        list=mDao.getAll() as MutableList<RadioListData>
        db.close()
        pageModelLiveData.postValue(list)
//        object : Thread() {
//            override fun run() {
//                pageModel.radioList=mDao.getAll() as MutableList<RadioListData>
//                pageModelLiveData.postValue(pageModel)
//            }
//        }.start()
    }

    fun addRadioList(name:String){
        val radioListData=RadioListData(Date().time,name,"")
        val db = Room.databaseBuilder(context, RadioListDatabase::class.java, "radio_list")
            .allowMainThreadQueries()
            .build()
        val mDao=db.radioListDao()
        object : Thread() {
            override fun run() {
                mDao.insert(radioListData)
                list.add(0, radioListData)
                pageModelLiveData.postValue(list)
                db.close()
            }
        }.start()
    }
}