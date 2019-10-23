package com.example.becast.home.wait

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.becast.data.radioDb.RadioData
import com.example.becast.data.radioDb.RadioDatabase

class WaitViewModel(private val context: Context) {

    private val list : MutableList<RadioData> = mutableListOf()
    val waitModelLiveData: MutableLiveData<MutableList<RadioData>> = MutableLiveData()

    init {
        waitModelLiveData.value=list
        object:Thread(){
            override fun run() {
                super.run()
                val db = Room.databaseBuilder(context, RadioDatabase::class.java, "radio")
                    .build()
                val mDao=db.radioDao()
                list.addAll(mDao.getWait() as MutableList<RadioData>)
                waitModelLiveData.postValue(list)
                db.close()
            }
        }.start()

    }

}