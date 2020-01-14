package com.example.becast.data.xml

import com.example.becast.data.UserData
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import io.reactivex.Observer
import okhttp3.*
import java.io.IOException
import java.lang.Exception

class XmlHttpHelper {

    fun getListFromNet(observer : Observer<MutableList<XmlData>>){
        val url= UserData.BaseUrl+"/xml/get"
        val formBody= FormBody.Builder()
            .add("uid", UserData.uid.toString())
            .build()
        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val dataString= response.body()!!.string()
                val xmlListData= Gson().fromJson(dataString , XmlListData::class.java)
                try{
                    Observable.just(xmlListData.xmlList).subscribe(observer)
                }catch (e:Exception){
                    observer.onComplete()
                }
            }
            override fun onFailure(call: Call, e: IOException) {

            }
        })
    }

    fun addToNet(xmlData: XmlData){
        val url= UserData.BaseUrl+"/xml/add"
        val requestBody = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"), Gson().toJson(xmlData))
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

    fun delList(uid:String,xmlUrl:String){
        val url= UserData.BaseUrl+"/xml/del"
        val formBody=FormBody.Builder()
            .add("uid",UserData.uid.toString())
            .add("xml_url",xmlUrl)
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

    data class XmlListData (
        @SerializedName("xml_list")
        var xmlList:MutableList<XmlData> = mutableListOf()
    )
}