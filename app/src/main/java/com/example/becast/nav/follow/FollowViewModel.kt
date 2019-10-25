package com.example.becast.nav.follow

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.becast.data.rss.RssData
import com.example.becast.unit.data.rssDB.RssDatabase

class FollowViewModel(private val context:Context) {

    val list : MutableList<RssData> = mutableListOf()
    val followModelLiveData: MutableLiveData<MutableList<RssData>> = MutableLiveData()
    init {
        followModelLiveData.value=list
    }

    fun getList(){
        object :Thread(){
            override fun run() {
                super.run()
                val db = Room.databaseBuilder(context, RssDatabase::class.java, "rss")
                    .allowMainThreadQueries()
                    .build()
                val mDao=db.rssDao()
                list.clear()
                list.addAll(mDao.getAll() as MutableList<RssData>)
                followModelLiveData.postValue(list)
                db.close()
            }
        }.start()
    }
}