package com.example.becast.nav.square

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import com.example.becast.nav.square.recommend.RecommendData
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class SquareViewModel {

    private var squareModel : RecommendData =
        RecommendData()
    val squareModelLiveData: MutableLiveData<RecommendData> = MutableLiveData()

    init {
        squareModelLiveData.value=squareModel
    }

    fun getJson(url:String,handler:Handler){
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .url("https://raw.githubusercontent.com/shihuafan/cubing/master/shf1211.json")
            .build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                handler.sendEmptyMessage(0x404)
            }
            override fun onResponse(call: Call, response: Response) {
                squareModel= Gson().fromJson(response.body()!!.string(), RecommendData::class.java)
                squareModelLiveData.postValue(squareModel)
            }
        })
    }
}