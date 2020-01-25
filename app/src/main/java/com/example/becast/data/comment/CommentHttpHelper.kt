package com.example.becast.data.comment

import android.content.ContentValues.TAG
import android.util.Log
import com.example.becast.data.UserData
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import io.reactivex.Observer
import okhttp3.*
import java.io.IOException

class CommentHttpHelper {

    data class CommentListData(
        @SerializedName("comment_list")
        val commentList:MutableList<CommentData>
    )

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

    fun addToNet(commentData: CommentData){
        val url= UserData.BaseUrl+"/comment/add"
        val requestBody = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"), Gson().toJson(commentData))
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

    fun delComment(commentData: CommentData){
        val url= UserData.BaseUrl+"/comment/del"
        val requestBody = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"), Gson().toJson(commentData))
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
}