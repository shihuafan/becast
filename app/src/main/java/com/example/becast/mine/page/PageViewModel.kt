package com.example.becast.mine.page

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.becast.data.radioList.RadioListData
import com.example.becast.data.radioList.RadioListDatabase
import java.util.*

class PageViewModel(private val context: Context) {
    val pageModel: PageModel = PageModel()
    val pageModelLiveData: MutableLiveData<PageModel> = MutableLiveData()

    init{
        pageModelLiveData.value=pageModel
        val db=Room.databaseBuilder(context,RadioListDatabase::class.java,"radio_list")
            .allowMainThreadQueries()
            .build()
        val mDao=db.radioListDao()
        pageModel.radioList=mDao.getAll() as MutableList<RadioListData>
        db.close()
        pageModelLiveData.postValue(pageModel)
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
                pageModel.radioList.add(0, radioListData)
                pageModelLiveData.postValue(pageModel)
                db.close()
            }
        }.start()
    }
}