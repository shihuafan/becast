package com.example.becast.nav.love

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.becast.data.mix.MixData
import com.example.becast.data.mix.MixDatabase
import com.example.becast.data.radio.RadioDatabase

class LoveViewModel {

    private val list : MutableList<MixData> = mutableListOf()
    val loveModelLiveData: MutableLiveData<MutableList<MixData>> = MutableLiveData()

    init {
        loveModelLiveData.value=list
    }

    fun getMix(context:Context){
        object : Thread(){
            override fun run() {
                super.run()
                val mixDb = MixDatabase.getDb(context)
                val mixDao=mixDb.mixDao()
                val radioDb=RadioDatabase.getDb(context)
                val radioDao=radioDb.radioDao()
                list.addAll(mixDao.getAll() as MutableList<MixData>)
                for(mix in list){
                   mix.imageUrl=radioDao.getImageUrlByMix(mix.time)
                }
                list.add(0,MixData(mix="我喜欢",mixId = "0"))
                loveModelLiveData.postValue(list)
                MixDatabase.closeDb()
            }
        }.start()
    }

//    fun createMix(context:Context,name:String){
//        val mixData=MixData(name,time=System.currentTimeMillis())
//        list.add(mixData)
//        loveModelLiveData.value=list
//
//        object:Thread(){
//            override fun run() {
//                super.run()
//                val db= MixDatabase.getDb(context)
//                val mDao=db.mixDao()
//                mDao.insert(mixData)
//                MixDatabase.closeDb()
//            }
//        }.start()
//    }

}