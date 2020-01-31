package com.example.becast.nav.user

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Handler
import android.os.Message
import android.util.Log
import android.util.Xml
import com.example.becast.data.Becast
import com.example.becast.data.UserData
import com.example.becast.data.radio.RadioDatabase
import com.example.becast.data.xml.XmlDatabase
import okhttp3.*
import okio.BufferedSink
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class UserViewModel {

    fun timeToStr(time: Int): String {
        return "${time/1000/3600}小时"
    }

    fun getUserData(context: Context,handler: Handler){
        object :Thread(){
            override fun run() {
                super.run()
                val xmlDb=XmlDatabase.getDb(context)
                val xmlDao=xmlDb.xmlDao()
                UserData.xmlSum=xmlDao.getSum()
                XmlDatabase.closeDb()

                val radioDb= RadioDatabase.getDb(context)
                val radioDao=radioDb.radioDao()
                UserData.radioSum=radioDao.getSum()
                UserData.timeSum=radioDao.getSumTime()
                RadioDatabase.closeDb()

                handler.sendEmptyMessage(Becast.RESTART)
            }
        }.start()
    }

    fun serializeXml(context: Context,handler:Handler){
        object :Thread(){
            override fun run() {
                super.run()
                val xmlDatabase=XmlDatabase.getDb(context)
                val xmlDao=xmlDatabase.xmlDao()
                val list=xmlDao.getAll()
                XmlDatabase.closeDb()
                val path=context.externalCacheDir
                val file= File(path.path+"/xml/")
                if(!file.exists()){
                    file.mkdirs()
                }
                val xmlFile=File(path.path+"/xml/"+System.currentTimeMillis()+".opml")
                xmlFile.createNewFile()
                val serializer = Xml.newSerializer()
                val outputStream= FileOutputStream(xmlFile)

                serializer.setOutput(outputStream, "utf-8")
                serializer.startDocument("utf-8", true)
                serializer.startTag(null,"opml")
                serializer.startTag(null,"body")
                for(item in list){
                    serializer.startTag(null,"outline")
                    serializer.attribute(null,"text",item.title)
                    serializer.attribute(null,"title",item.title)
                    serializer.attribute(null,"type","rss")
                    serializer.attribute(null,"xmlUrl",item.xmlUrl)
                    serializer.attribute(null,"htmlUrl",item.link)
                    serializer.endTag(null,"outline")
                }
                serializer.endTag(null,"body")
                serializer.endTag(null,"opml")
                serializer.endDocument()
                outputStream.close()
                val msg=Message()
                msg.obj=xmlFile.path.toString()
                msg.what=Becast.OUTPUT_OPML
                handler.sendMessage(msg)
            }
        }.start()
    }

    fun share(context: Context,path:String){
        var shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, path)
        shareIntent = Intent.createChooser(shareIntent, "Share Opml to ......")
        context.startActivity(shareIntent)
    }

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

    fun changeName(context: Context,handler: Handler,name:String){
        UserData.change(context,name=name)
        handler.sendEmptyMessage(Becast.RESTART)
        val url= UserData.BaseUrl+"/User/change"
        val body=FormBody.Builder()
            .add("uid",UserData.uid.toString())
            .add("name",name)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if(response.body()!!.string()=="success"){
                    handler.sendEmptyMessage(Becast.RESTART)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                handler.sendEmptyMessage(Becast.RESTART)
            }
        })
    }

    fun uploadImage(context: Context,bitmap:Bitmap,handler: Handler){
        val filename= UserData.uid+"_"+System.currentTimeMillis().toString()+".jpg"
        val imageUrl=UserData.BaseUrl+"/image/get/"+filename
        UserData.change(context,image = imageUrl)
        val url= UserData.BaseUrl+"/image/add"
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("image", "image",BitmapRequestBody(bitmap))
            .addFormDataPart("uid", UserData.uid.toString())
            .addFormDataPart("filename",filename)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if(response.body()!!.string()=="success"){
                    handler.sendEmptyMessage(Becast.RESTART)
                }
            }
            override fun onFailure(call: Call, e: IOException) {

            }
        })
    }

    class BitmapRequestBody(
        private val bitmap: Bitmap,
        private val format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    ): RequestBody(){

        override fun contentType(): MediaType? {
            return MediaType.parse(when(format){
                Bitmap.CompressFormat.WEBP -> "image/webp"
                Bitmap.CompressFormat.PNG -> "image/png"
                else -> "image/jpeg"
            })
        }

        override fun writeTo(sink: BufferedSink) {
            bitmap.compress(format,100,sink.outputStream())
        }

    }
}