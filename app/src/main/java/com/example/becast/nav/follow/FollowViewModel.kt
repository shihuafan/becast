package com.example.becast.nav.follow

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.becast.data.rss.RssData
import com.example.becast.data.rss.RssDatabaseHelper

class FollowViewModel {

    val list : MutableList<RssData> = mutableListOf()
    val followModelLiveData: MutableLiveData<MutableList<RssData>> = MutableLiveData()

    init {
        followModelLiveData.value=list
    }

    fun getList(context:Context){
        object :Thread(){
            override fun run() {
                super.run()
                val db = RssDatabaseHelper.getDb(context)
                val mDao=db.rssDao()
                list.clear()
                list.addAll(mDao.getAll() as MutableList<RssData>)
                followModelLiveData.postValue(list)
                RssDatabaseHelper.closeDb()
            }
        }.start()
    }
}