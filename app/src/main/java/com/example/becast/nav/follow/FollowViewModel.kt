package com.example.becast.nav.follow

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.becast.data.rss.RssData
import com.example.becast.unit.data.radioDb.RadioData
import com.example.becast.unit.data.radioDb.RadioDatabase
import com.example.becast.unit.data.rssDB.RssDatabase
import java.lang.Exception
import java.util.*

class FollowViewModel(private val context:Context) {

    var list : MutableList<RssData> = mutableListOf()
    val followModelLiveData: MutableLiveData<MutableList<RssData>> = MutableLiveData()
    init {
        followModelLiveData.value=list
        val db = Room.databaseBuilder(context, RssDatabase::class.java, "rss")
            .allowMainThreadQueries()
            .build()
        val mDao=db.rssDao()
        list =mDao.getAll() as MutableList<RssData>
        followModelLiveData.value=list
        db.close()
    }

}