package com.example.becast.home.wait

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.becast.data.radioDb.RadioData
import com.example.becast.data.radioDb.RadioDatabase

class WaitViewModel(private val context: Context) {

    private var list : MutableList<RadioData> = mutableListOf()
    val waitModelLiveData: MutableLiveData<MutableList<RadioData>> = MutableLiveData()

    init {
        waitModelLiveData.value=list
        val db = Room.databaseBuilder(context, RadioDatabase::class.java, "radio")
            .allowMainThreadQueries()
            .build()
        val mDao=db.radioDao()
        list =mDao.getWait() as MutableList<RadioData>
        waitModelLiveData.value=list
        db.close()
    }

}