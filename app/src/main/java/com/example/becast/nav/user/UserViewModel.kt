package com.example.becast.nav.user

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Handler
import android.util.Log
import com.example.becast.data.Becast
import com.example.becast.data.radio.RadioDatabase
import com.example.becast.data.xml.XmlDatabase

class UserViewModel {

    fun deleteAll(context: Context,handler: Handler){
        object:Thread(){
            override fun run() {
                super.run()
                val xmlDb=XmlDatabase.getDb(context)
                val xmlDao=xmlDb.xmlDao()
                xmlDao.deleteAll()
                XmlDatabase.closeDb()
                Log.d(TAG,"清空数据库")
                val radioDb= RadioDatabase.getDb(context)
                val radioDao=radioDb.radioDao()
                radioDao.deleteAll()
                RadioDatabase.closeDb()
                handler.sendEmptyMessage(Becast.RESTART_APP)
            }
        }.start()
    }

}