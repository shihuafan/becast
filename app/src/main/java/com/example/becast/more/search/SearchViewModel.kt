package com.example.becast.more.search

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import com.example.becast.data.rss.RssData
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class SearchViewModel(term:String,private val handler: Handler) {

    val list : MutableList<RssData> = mutableListOf()
    val  listLiveData: MutableLiveData<MutableList<RssData>> = MutableLiveData()

    init {
        listLiveData.value=list
        val url= "https://itunes.apple.com/search?term=$term&entity=podcast"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val content=response.body()!!.string()
                readJson(content)
            }
            override fun onFailure(call: Call, e: IOException) {

            }
        })
    }

    fun readJson(json:String){
        val jsonArray = JSONObject(json).getJSONArray("results")
        for(i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.get(i) as JSONObject
            val xmlUrl=jsonObject.getString("feedUrl")
            val imageUri=jsonObject.getString("artworkUrl60")
            val title=jsonObject.getString("collectionName")
            val rssData = RssData(title,"",imageUri,"","",xmlUrl,"","")
            list.add(rssData)
        }
        listLiveData.postValue(list)
    }
}