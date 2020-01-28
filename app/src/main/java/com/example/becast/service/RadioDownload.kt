package com.example.becast.service

import android.content.Context
import com.example.becast.data.radio.RadioData
import com.example.becast.data.radio.RadioDatabase
import com.example.becast.radiodownload.RadioDownLoadTask

class RadioDownload(private val context:Context) {

    private val runningList= mutableListOf<RadioData>()
    private val waitList= mutableListOf<RadioData>()

    fun downLoad(radioData: RadioData){
        val path=context.getExternalFilesDir("")
        val filename=radioData.radioUrl.hashCode()
        radioData.downloadPath=path!!.path+"/download/"+filename+".apk"
        if(runningList.size<3){
            runningList.add(radioData)
            val radioDownLoad= RadioDownLoadTask()
            val downLoadListener=DownLoadListener()
            radioDownLoad.setListener(downLoadListener)
            radioDownLoad.execute(radioData)
        }
        else{
            waitList.add(radioData)
        }
        println(runningList)
        println(waitList)
    }

    inner class DownLoadListener{

        fun update(radioData: RadioData){
            val radioDatabase= RadioDatabase.getDb(context)
            val radioDao=radioDatabase.radioDao()
            radioDao.updateItem(radioData)
        }

        fun finish(radioData: RadioData){
            runningList.remove(radioData)
            if(runningList.size<4 && waitList.size>0){
                runningList.add(waitList.removeAt(0))
            }
        }
    }
}