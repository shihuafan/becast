package com.example.becast.nav.setting.download

import android.os.AsyncTask
import com.example.becast.data.UserData
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DownLoadFromNet: AsyncTask<String,Int,String>() {

    private var listener:DownLoadService.DownLoadListener?=null
    private var present=0

    fun setListener(listener: DownLoadService.DownLoadListener){
        this.listener=listener
    }

    override fun doInBackground(vararg params: String?): String {
        val url= UserData.BaseUrl +"/download"
        val max=getContentLength(url).toInt()
        val path=params[0].toString()
        val file= File(path)
        if(file.exists()){
            file.delete()
        }
        file.createNewFile()
        println(file)
        val outputStream = FileOutputStream(file)
        val request= Request.Builder()
            .url(url)
            .get()
            .build()
        val call= OkHttpClient().newCall(request)
        val response=call.execute()
        val inputStream = response.body()?.byteStream()
        inputStream?.let {
            val bytes = ByteArray(1024)
            var readLength= inputStream.read(bytes)
            var progress=0
            while (readLength != -1) {
                outputStream.write(bytes,0,readLength)
                progress += readLength
                publishProgress(max,progress)
                readLength=inputStream.read(bytes)
            }
            inputStream.close()
            outputStream.close()
        }
        return path
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        val max= values[0]!!.toInt()
        val progress=values[1]!!.toInt()
        if(max!=0 && (100*progress/max)!=present){
            present=100*progress/max
            listener?.setNotification(present)
        }
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        listener?.finish(result)
    }

    @Throws(IOException::class)
    private fun getContentLength(url: String): Long {
        val request = Request.Builder().url(url).build()
        val call=OkHttpClient().newCall(request)
        val response = call.execute()
        var contentLength=0L
        if (response.isSuccessful) {
            response.body()?.contentLength()?.let { contentLength=it }
            response.close()
        }
        return contentLength
    }
}