package com.example.becast.nav.love

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.becast.data.mix.MixData
import com.example.becast.data.mix.MixDatabase

class LoveViewModel {

    private val list : MutableList<MixData> = mutableListOf()
    val loveModelLiveData: MutableLiveData<MutableList<MixData>> = MutableLiveData()

    init {
        list.add(MixData("","我喜欢","我喜欢",1L))
        loveModelLiveData.value=list
    }

    fun getMix(context:Context){
        object : Thread(){
            override fun run() {
                super.run()
                val db = MixDatabase.getDb(context)
                val mDao=db.mixDao()
                list.addAll(mDao.getAll() as MutableList<MixData>)
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