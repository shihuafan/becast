package com.example.becast.playpage.play

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.room.Room
import com.example.becast.data.radioDb.RadioData
import com.example.becast.data.radioDb.RadioDatabase
import com.example.becast.data.rss.RssData
import com.example.becast.unit.data.rssDB.RssDatabase
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
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

    fun createQRCodeBitmap(content: String): Bitmap? {
        val width=400
        val height=400
        try {
            val hints = Hashtable<EncodeHintType, String>()

            hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
            hints[EncodeHintType.ERROR_CORRECTION] = "L"
            hints[EncodeHintType.MARGIN] = "1"

            val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints)

            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                for (x in 0 until width) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = Color.BLACK//黑色色块像素设置
                    } else {
                        pixels[y * width + x] = Color.WHITE// 白色色块像素设置
                    }
                }
            }
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            return bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            return null
        }
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

    fun getRssData(context: Context,radioData: RadioData): RssData {
        val db= Room.databaseBuilder(context, RssDatabase::class.java,"rss")
            .allowMainThreadQueries()
            .build()
        val mDao=db.rssDao()
        val temp=mDao.getRssData(radioData.rssUri)
        db.close()
        return temp

    }

}