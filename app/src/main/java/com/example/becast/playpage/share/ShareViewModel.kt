package com.example.becast.playpage.share

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Handler
import android.os.Message
import androidx.palette.graphics.Palette
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*

class ShareViewModel{


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

    fun getBackgroundColor(handler: Handler,url:String){
        object :Thread(){
            override fun run() {
                super.run()
                val request= Request.Builder()
                    .url(url)
                    .method("GET",null)
                    .build()
                val okHttpClient= OkHttpClient()
                val response=okHttpClient.newCall(request).execute()
                val inputStream = response.body()!!.byteStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                Palette.from(bitmap).generate {
//                    val array:Array<Int?> = arrayOf(
//                        it?.getDarkMutedColor(Color.BLUE),//如果分析不出来，则返回默认颜色
//                        it?.getLightMutedColor(Color.BLUE),
//                        it?.getDarkVibrantColor(Color.BLUE),
//                        it?.getLightVibrantColor(Color.BLUE),
//                        it?.getMutedColor(Color.BLUE),
//                        it?.getVibrantColor(Color.BLUE)
//                    )

                    val msg=Message()
                    msg.obj= it?.getVibrantColor(Color.WHITE)
                    handler.sendMessage(msg)
                }
            }
        }.start()

    }
}