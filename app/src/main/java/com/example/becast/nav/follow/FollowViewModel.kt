package com.example.becast.nav.follow

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.becast.data.xml.XmlData
import com.example.becast.data.xml.XmlDatabase

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
                val db = XmlDatabase.getDb(context)
                val mDao=db.xmlDao()
                list.clear()
                list.addAll(mDao.getAll() as MutableList<XmlData>)
                followModelLiveData.postValue(list)
                XmlDatabase.closeDb()
            }
        }.start()
    }
}