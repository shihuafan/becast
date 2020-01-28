package com.example.becast.radiodownload

import android.os.AsyncTask
import com.example.becast.data.radio.RadioData
import com.example.becast.service.RadioDownload
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class RadioDownLoadTask: AsyncTask<RadioData,RadioData,RadioData>() {

    private var listener: RadioDownload.DownLoadListener?=null
    private var present=0

    fun setListener(listener: RadioDownload.DownLoadListener){
        this.listener=listener
    }

    override fun doInBackground(vararg params: RadioData?): RadioData {
        val radioData=params[0]!!
        //已下载完成
        if(radioData.downloadMax!=0 && radioData.downloadProgress==radioData.downloadMax){
            return radioData
        }
        val url= radioData.radioUrl
        val path=radioData.downloadPath
        radioData.downloadMax = getContentLength(url).toInt()
        val file= File(path)
        if(file.exists()){
            file.delete()
        }
        file.createNewFile()
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
            radioData.downloadProgress=0
            while (readLength != -1) {
                outputStream.write(bytes,0,readLength)
                radioData.downloadProgress += readLength
                publishProgress(radioData)
                readLength=inputStream.read(bytes)
            }
            inputStream.close()
            outputStream.close()
        }
        return RadioData()
    }

    override fun onProgressUpdate(vararg values: RadioData?) {
        listener?.update(values[0]!!)
    }

    override fun onPostExecute(result: RadioData?) {
        super.onPostExecute(result)
        result?.let { listener?.finish(it) }
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