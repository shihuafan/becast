package com.example.becast.nav.square

import com.example.becast.data.RecommendData
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.Observer
import okhttp3.*
import java.io.IOException

class SquareViewModel {

    var recommend : RecommendData= RecommendData()

    fun getJson(url:String,observer:Observer<RecommendData>){
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url("https://raw.githubusercontent.com/shihuafan/cubing/master/shf1211.json")
            .build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Observable.error<RecommendData>(e)
                    .subscribe(observer)
            }
            override fun onResponse(call: Call, response: Response) {
                recommend= Gson().fromJson(response.body()!!.string(), RecommendData::class.java)
                Observable.just(recommend)
                    .subscribe(observer)
            }
        })
    }
}