package com.example.becast.nav.love

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.becast.data.mix.MixData
import com.example.becast.data.mix.MixDatabaseHelper
import com.example.becast.data.radioDb.RadioData
import com.example.becast.data.radioDb.RadioDatabase
import com.example.becast.data.radioDb.RadioDatabaseHelper
import java.lang.Exception
import java.util.*

class LoveViewModel(private val context:Context) {

    private val list : MutableList<MixData> = mutableListOf()
    val loveModelLiveData: MutableLiveData<MutableList<MixData>> = MutableLiveData()

    init {
        loveModelLiveData.value=list
        list.add(MixData("我喜欢",1L))
        object : Thread(){
            override fun run() {
                super.run()
                val db = MixDatabaseHelper.getDb(context)
                val mDao=db.mixDao()
                list.addAll(mDao.getAll() as MutableList<MixData>)
                loveModelLiveData.postValue(list)
                MixDatabaseHelper.closeDb()
            }
        }.start()
    }

    fun createMix(name:String){
        val mixData=MixData(name,System.currentTimeMillis())
        list.add(mixData)
        loveModelLiveData.value=list
        object:Thread(){
            override fun run() {
                super.run()
                val db=MixDatabaseHelper.getDb(context)
                val mDao=db.mixDao()
                mDao.insert(mixData)
                MixDatabaseHelper.closeDb()
            }
        }.start()
    }

}