package com.example.becast.nav.follow

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.becast.data.xml.XmlData
import com.example.becast.data.xml.XmlDatabaseHelper

class FollowViewModel {

    val list : MutableList<XmlData> = mutableListOf()
    val followModelLiveData: MutableLiveData<MutableList<XmlData>> = MutableLiveData()

    init {
        followModelLiveData.value=list
    }

    fun getList(context:Context){
        object :Thread(){
            override fun run() {
                super.run()
                val db = XmlDatabaseHelper.getDb(context)
                val mDao=db.xmlDao()
                list.clear()
                list.addAll(mDao.getAll() as MutableList<XmlData>)
                followModelLiveData.postValue(list)
                XmlDatabaseHelper.closeDb()
            }
        }.start()
    }
}