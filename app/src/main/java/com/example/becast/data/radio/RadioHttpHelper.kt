package com.example.becast.data.radio

import com.example.becast.data.UserData
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import io.reactivex.Observer
import okhttp3.*
import java.io.IOException
import java.lang.Exception

class RadioHttpHelper {


    fun getListFromNet(observer : Observer<MutableList<RadioData>>){
        val url= UserData.BaseUrl+"/radio/get"
        val formBody= FormBody.Builder()
            .add("uid", UserData.uid.toString())
            .build()
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val dataString= response.body()!!.string()
                val radioListData= Gson().fromJson(dataString , RadioListData::class.java)
                try{
                    Observable.just(radioListData.radioList).subscribe(observer)
                }catch (e:Exception){
                    observer.onComplete()
                }
            }
            override fun onFailure(call: Call, e: IOException) {

            }
        })
    }

    fun addToNet(radioList: MutableList<RadioData>){
        val radioListData=RadioListData(radioList)
        val url= UserData.BaseUrl+"/radio/add"
        val requestBody = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"), Gson().toJson(radioListData))
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
        val url= UserData.BaseUrl+"/radio/del"
        val formBody=FormBody.Builder()
            .add("uid",UserData.uid.toString())
            .add("radio_url",xmlUrl)
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

    data class RadioListData (
        @SerializedName("radio_list")
        var radioList:MutableList<RadioData>
    )
}