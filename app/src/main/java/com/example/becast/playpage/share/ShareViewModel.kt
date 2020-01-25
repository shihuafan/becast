package com.example.becast.playpage.share

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.view.View
import androidx.palette.graphics.Palette
import com.example.becast.data.Becast
import com.example.becast.data.UserData
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import okhttp3.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

class ShareViewModel{

    companion object{
        const val TAG_QQ=1
        const val TAG_WeChat=2
        const val TAG_TimeLine=3
    }

    fun createQRCodeBitmap(content: String): Bitmap? {
        val width=500
        val height=500
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

    fun getShareId(shareData: ShareData){
        val url=UserData.BaseUrl+"/share/add"
        val requestBody = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"), Gson().toJson(shareData))
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
            }
            override fun onFailure(call: Call, e: IOException) {
            }
        })
    }

    fun shareTo(context: Context, view: View,tag:Int){
        when(tag){
            TAG_QQ->{
                shareToQQ(context,view)
            }
            TAG_WeChat->{
                shareToWeChat(context,view)
            }
            TAG_TimeLine->{
                shareToTimeLine(context,view)
            }

        }
    }

    fun shareToQQ(context: Context, view: View){

        val bitmap=getBitmapToView(view)

        val url = Uri.parse(
            MediaStore.Images.Media.insertImage(
                context.contentResolver, bitmap, null, null
            )
        )
        val shareIntent = Intent()
        val componentName = ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity")
        shareIntent.component = componentName
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, url)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(Intent.createChooser(shareIntent, "sharePhotoToQQ"))
    }

    fun shareToWeChat(context: Context, view: View){

        val bitmap=getBitmapToView(view)

        val url = Uri.parse(
            MediaStore.Images.Media.insertImage(
                context.contentResolver, bitmap, null, null
            )
        )
        val shareIntent = Intent()
        val componentName = ComponentName("com.tencent.mm","com.tencent.mm.ui.tools.ShareImgUI")
        shareIntent.component = componentName
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, url)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(Intent.createChooser(shareIntent, "sharePhotoToWeChat"))
    }

    fun shareToTimeLine(context: Context, view: View){

        val bitmap=getBitmapToView(view)

        val url = Uri.parse(
            MediaStore.Images.Media.insertImage(
                context.contentResolver, bitmap, null, null
            )
        )
        val shareIntent = Intent()
        val componentName = ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI")
        shareIntent.component = componentName
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, url)
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(Intent.createChooser(shareIntent, "sharePhotoToWeChat"))
    }

    private fun getBitmapToView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        view.draw(Canvas(bitmap))
        return bitmap
    }
}