package com.example.becast.service.download

import android.content.Context
import com.example.becast.data.radio.RadioData
import com.example.becast.data.radio.RadioDatabase
import java.io.File

class RadioDownload(private val context:Context) {


    private val runningList= mutableListOf<RadioData>()
    private val waitList= mutableListOf<RadioData>()

    init {
        object :Thread(){
            override fun run() {
                super.run()
                val radioDatabase=RadioDatabase.getDb(context)
                val radioDao=radioDatabase.radioDao()
                val tempList=radioDao.getDownLoad()
                RadioDatabase.closeDb()
                for(radioData in tempList){
                    downLoad(radioData)
                }
            }
        }.start()

    }
    fun downLoad(radioData: RadioData){
        val path=context.getExternalFilesDir("")
        val file= File(path!!.path+"/download")
        if(!file.exists()){
            file.mkdir()
        }
        if(runningList.size<3){
            runningList.add(radioData)
            val radioDownLoadTask= RadioDownLoadTask()
            val downLoadListener=DownLoadListener()
            radioDownLoadTask.setListener(downLoadListener)
            radioDownLoadTask.execute(radioData)
        }
        else{
            waitList.add(radioData)
        }
        println(runningList)
        println(waitList)
    }

    inner class DownLoadListener{

        fun finish(radioData: RadioData){
            radioData.downloadFinish=true
            println(radioData)
            object :Thread(){
                override fun run() {
                    super.run()
                    val radioDatabase= RadioDatabase.getDb(context)
                    val radioDao=radioDatabase.radioDao()
                    radioDao.updateItem(radioData)
                    RadioDatabase.closeDb()
                }
            }.start()
            runningList.remove(radioData)
            if(runningList.size<4 && waitList.size>0){
                runningList.add(waitList.removeAt(0))
            }
        }
    }
}