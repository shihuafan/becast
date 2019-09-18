package com.example.becast.playpage

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.text.TextUtils
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import java.lang.Exception
import java.util.*
import kotlin.experimental.and
import java.security.MessageDigest as MessageDigest1


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
//    fun createQRCodeBitmap(content: String, width: Int, height: Int, character_set: String, error_correction_level: String,
//                           margin: String, color_black: Int, color_white: Int): Bitmap? {
//
//        if (TextUtils.isEmpty(content)) {
//            return null
//        }
//
//        if (width < 0 || height < 0) {
//            return null
//        }
//        try {
//            /** 1.设置二维码相关配置  */
//            val hints = Hashtable<EncodeHintType, String>()
//            // 字符转码格式设置
//            if (!TextUtils.isEmpty(character_set)) {
//                hints[EncodeHintType.CHARACTER_SET] = character_set
//            }
//            // 容错率设置
//            if (!TextUtils.isEmpty(error_correction_level)) {
//                hints[EncodeHintType.ERROR_CORRECTION] = error_correction_level
//            }
//            // 空白边距设置
//            if (!TextUtils.isEmpty(margin)) {
//                hints[EncodeHintType.MARGIN] = margin
//            }
//            /** 2.将配置参数传入到QRCodeWriter的encode方法生成BitMatrix(位矩阵)对象  */
//            val bitMatrix =
//                QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints)
//
//            /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值  */
//            val pixels = IntArray(width * height)
//            for (y in 0 until height) {
//                for (x in 0 until width) {
//                    //bitMatrix.get(x,y)方法返回true是黑色色块，false是白色色块
//                    if (bitMatrix.get(x, y)) {
//                        pixels[y * width + x] = color_black//黑色色块像素设置
//                    } else {
//                        pixels[y * width + x] = color_white// 白色色块像素设置
//                    }
//                }
//            }
//            /** 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,并返回Bitmap对象  */
//            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
//            return bitmap
//        } catch (e: WriterException) {
//            e.printStackTrace()
//            return null
//        }
//    }
}