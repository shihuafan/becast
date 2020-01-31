package com.example.becast.playpage.play

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Message
import com.example.becast.data.radio.RadioData
import com.example.becast.data.radio.RadioDatabase
import com.example.becast.service.MediaHelper
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.experimental.and


class PlayPageViewModel {

    fun timeToStr(time: Int): String {
        return if (time % 60 < 10) {
            (time / 60).toString() + ":0" + time % 60
        } else {
            (time / 60).toString() + ":" + time % 60
        }
    }

    fun download(context: Context, radioData: RadioData){
        object :Thread(){
            override fun run() {
                super.run()
                val path=context.getExternalFilesDir("")
                val filename=radioData.radioUrl.hashCode()
                radioData.downloadPath=path!!.path+"/download/"+filename+".mp3"
                val radioDatabase=RadioDatabase.getDb(context)
                val radioDao=radioDatabase.radioDao()
                radioDao.updateItem(radioData)
                RadioDatabase.closeDb()
                MediaHelper().getDownload()?.downLoad(radioData)
            }
        }.start()
    }

    @SuppressLint("DefaultLocale")
    fun md5(password:String):String{
        try{
            val md5=java.security.MessageDigest.getInstance("MD5")
            val bytes=md5.digest(password.toByteArray())
            var result=""
            for(b in bytes){
                var temp=Integer.toHexString((b and 0xff.toByte()).toInt())
                if(temp.length==1){
                    temp= "0$temp"
                }
                result+=temp
            }
            return result.toUpperCase()
        }catch(e:Exception){}
        return " "
    }

//    fun getSharePic(radioData: RadioData,handler: Handler){
//        object :Thread(){
//            override fun run() {
//                super.run()
//                try {
//                    val request= Request.Builder()
//                        .url(radioData.xmlImageUrl)
//                        .method("GET",null)
//                        .build()
//                    val okHttpClient= OkHttpClient()
//                    val response=okHttpClient.newCall(request).execute()
//                    val inputStream = response.body()!!.byteStream()
//                    var bitmap = BitmapFactory.decodeStream(inputStream)
//                    bitmap=size(bitmap,1000,1000)
//                    val canvas= Canvas(bitmap)
//                    val qrBitmap= createQRCodeBitmap("shihuafan")?.let { size(it,150,150) }
//                    canvas.drawBitmap(qrBitmap,700f,700f,null)
//                    val paint=Paint()
//                    paint.color=Color.WHITE
//                    paint.textSize=24f
//                    canvas.drawText(radioData.title,100f,100f,paint)
//
//                    val msg=Message()
//                    msg.obj=bitmap
//                    handler.sendMessage(msg)
//                } catch (e: IOException) { }
//            }
//        }.start()
//    }




}