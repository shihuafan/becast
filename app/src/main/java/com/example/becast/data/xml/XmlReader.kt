package com.example.becast.data.xml

import android.annotation.SuppressLint
import android.util.Xml
import com.example.becast.data.UserData
import com.example.becast.data.radio.RadioData
import io.reactivex.Observable
import io.reactivex.Observer
import okhttp3.*
import org.xmlpull.v1.XmlPullParser
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class XmlReader{

    lateinit var xmlData:XmlData
    val radioList= mutableListOf<RadioData>()

     fun getXml(url:String,observe:Observer<String>){
        xmlData= XmlData(uid= UserData.uid.toString(),xmlUrl = url)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object : Callback {
            @SuppressLint("CheckResult")
            override fun onResponse(call: Call, response: Response) {
                val input= response.body()!!.byteStream()
                try {
                    readXml(input,observe)
                    input.close()
                }catch (e:Exception){
                    observe.onError(e)
                }
            }
            @SuppressLint("CheckResult")
            override fun onFailure(call: Call, e: IOException) {
                observe.onError(e)
            }
        })
    }

    private fun readXml(input: InputStream,observe:Observer<String>){
        var radioData = RadioData()
        var flag=true
        val parser= Xml.newPullParser()
        parser.setInput(input,"UTF-8")
        var eventType=parser.eventType

        while (eventType!= XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_DOCUMENT -> {
                    // opmlList = mutableListOf()
                }
                XmlPullParser.START_TAG -> {
                    if("item"==parser.name){
                        radioData= RadioData(
                            uid= UserData.uid.toString(),
                            imageUrl = xmlData.imageUrl,
                            xmlImageUrl = xmlData.imageUrl,
                            xmlUrl = xmlData.xmlUrl,
                            xmlTitle = xmlData.title)
                        flag=false
                    }
                    if(flag){
                        when(parser.name){
                            "title"->xmlData.title=parser.nextText()
                            "link"->xmlData.link=parser.nextText()
                            "pubDate"->xmlData.pubDate=parser.nextText()
                            "image"->xmlData.imageUrl=parser.getAttributeValue(null,"href")
                            "description"->xmlData.description=parser.nextText()
                            "author"->xmlData.author=parser.nextText()
                            "language"->xmlData.language=parser.nextText()
                        }
                    }else
                    {
                        when(parser.name){
                            "title"->radioData.title=parser.nextText()
                            "duration"->radioData.duration=parser.nextText()
                            "enclosure"->radioData.radioUrl=parser.getAttributeValue(null,"url")
                            "image"->radioData.imageUrl=parser.getAttributeValue(null,"href")
                            "description"->radioData.description=parser.nextText()
                            "pubDate"->radioData.pubDate=parser.nextText()
                            "link"->radioData.link=parser.nextText()
                        }
                    }
                }
                XmlPullParser.END_TAG -> {
                    if ("item" == parser.name) {
                        radioData.upDate=getLongTime(radioData.pubDate)
                        radioList.add(radioData)
                    }
                }
            }
            eventType = parser.next()
        }
        observe.onComplete()
    }

    private fun getLongTime(str:String):Long{
        val sdf = SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
        return try {
            val date = sdf.parse(str)
            date.time
        } catch (e: Exception) {
            0
        }
    }
}