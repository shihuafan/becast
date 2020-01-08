package com.example.becast.data.comment

import com.example.becast.data.UserData
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.Observer
import okhttp3.*
import java.io.IOException
import java.lang.Exception

class CommentHttpHelper {

    class CommentListData{
        val commentList= mutableListOf<CommentData>()
    }

    fun getComment(xmlUrl:String,radioUrl:String,observer: Observer<MutableList<CommentData>>){
        val url= UserData.BaseUrl+"/comment/get"
        val formBody= FormBody.Builder()
            .add("xml_url", xmlUrl)
            .add("radio_url", radioUrl)
            .build()
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val dataString= response.body()!!.string()
                val commentListData= Gson().fromJson(dataString , CommentListData::class.java)
                try{
                    Observable.just(commentListData.commentList).subscribe(observer)
                }catch (e:Exception){
                    observer.onError(e)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                observer.onError(e)
            }
        })
    }

    fun delComment(id:Int){
        val url= UserData.BaseUrl+"/comment/del"
        val formBody=FormBody.Builder()
            .add("id", 1.toString())
            .build()
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {

            }
            override fun onFailure(call: Call, e: IOException) {

            }
        })
    }
}