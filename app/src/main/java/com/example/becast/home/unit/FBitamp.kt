package com.example.becast.home.unit

import android.graphics.*
import android.widget.ImageView
import java.io.IOException
import kotlin.math.min
import okhttp3.OkHttpClient
import okhttp3.Request
import android.os.Handler
import android.os.Message

class FBitamp{



    @Synchronized
    fun getBitmap(url:String,handler: Handler){
        object : Thread() {
            override fun run() {
                try {
                    val request=Request.Builder()
                        .url(url)
                        .method("GET",null)
                        .build()
                    val okHttpClient= OkHttpClient()
                    val response=okHttpClient.newCall(request).execute()
                    val inputStream = response.body()!!.byteStream()
                    val msg=Message()
                    msg.obj=BitmapFactory.decodeStream(inputStream)
                    handler.sendMessage(msg)
                } catch (e: IOException) {
                }
            }
        }.start()
    }

    fun size(newWidth: Int, newHeight: Int,bitmap:Bitmap): Bitmap {

        //获取宽高
        val height = bitmap.height
        val width = bitmap.width
        //获取宽高的缩放
        val scaleWidth =  newWidth.toFloat() /  width.toFloat()
        val scaleHeight =  newHeight.toFloat() / height.toFloat()

        val scale = if(scaleWidth>scaleHeight) scaleWidth
        else scaleHeight
        //获取裁剪的宽高
        val tWidth=(newWidth/scale).toInt()
        val tHeight=(newHeight/scale).toInt()
        //获取起始点
        val x=(width-tWidth)/2
        val y=(height-tHeight)/2

        val matrix = Matrix()
        matrix.postScale(scale, scale)// 使用后乘

        return Bitmap.createBitmap(bitmap, x, y, tWidth, tHeight, matrix,false)
    }

    fun roundedCorner(presenter: Float,bitmap:Bitmap): Bitmap {

        val radius=presenter*min(bitmap.height,bitmap.width)

        val result = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)

        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, radius, radius, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return result
    }

    
}