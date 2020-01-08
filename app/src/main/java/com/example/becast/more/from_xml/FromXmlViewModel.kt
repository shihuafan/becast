package com.example.becast.more.from_xml


import android.content.ContentValues.TAG
import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.Log
import android.util.Xml
import androidx.lifecycle.MutableLiveData
import com.example.becast.data.Becast
import com.example.becast.data.UserData
import com.example.becast.data.radio.RadioData
import com.example.becast.data.radio.RadioDatabase
import com.example.becast.data.radio.RadioHttpHelper
import com.example.becast.data.xml.XmlData
import com.example.becast.data.xml.XmlDatabase
import com.example.becast.data.xml.XmlHttpHelper
import com.example.becast.data.xml.XmlReader
import io.reactivex.Observer
import okhttp3.*
import org.xmlpull.v1.XmlPullParser
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import io.reactivex.internal.disposables.DisposableHelper.dispose
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableHelper.dispose

class FromXmlViewModel(val context: Context,private val url:String,private val handler: Handler) {

    private lateinit var xmlData: XmlData
    val xmlDataLiveData : MutableLiveData<XmlData> = MutableLiveData()
    private val radioList : MutableList<RadioData> = mutableListOf()
    private val radioListCache : MutableList<RadioData> = mutableListOf()
    val radioListLiveData : MutableLiveData<MutableList<RadioData>> = MutableLiveData()
    @Volatile
    var flag=true

    init {
        radioListLiveData.value = radioList
        getXml()
    }
    private fun getXml(){
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val input= response.body()!!.byteStream()
                try {
                    readXml(input)
                    input.close()
                }catch (e:Exception){
                    handler.sendEmptyMessage(Becast.FILE_ERROR)
                }

            }
            override fun onFailure(call: Call, e: IOException) {
                handler.sendEmptyMessage(Becast.NET_ERROR)
            }
        })
    }

    fun readXml(input: InputStream){

        xmlData = XmlData(uid=UserData.uid.toString(),xmlUrl = url)
        var radioData = RadioData()
        var flag=true
        val parser= Xml.newPullParser()
        parser.setInput(input,"UTF-8")
        var eventType=parser.eventType

        while (eventType!= XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_DOCUMENT -> {
                }
                XmlPullParser.START_TAG -> {
                    if("item"==parser.name){
                        radioData= RadioData(
                            uid=UserData.uid.toString(),
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
                        radioListCache.add(radioData)
                    }
                    if(radioListCache.size==50){
                        radioList.clear()
                        radioList.addAll(radioListCache)
                        xmlDataLiveData.postValue(xmlData)
                        radioListLiveData.postValue(radioList)
                    }
                }
            }
            eventType = parser.next()
        }
        if(radioListCache.size<50){
            radioList.clear()
            radioList.addAll(radioListCache)
            xmlDataLiveData.postValue(xmlData)
            radioListLiveData.postValue(radioList)
        }
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

    fun getMore(){
        if(flag){
            object:Thread(){
                override fun run() {
                    super.run()
                    if(radioListCache.size-radioList.size>30){
                        radioList.addAll(radioListCache.subList(radioList.size,radioList.size+30))
                    }else{
                        radioList.addAll(radioListCache.subList(radioList.size,radioListCache.size))
                        flag=false
                    }
                    radioListLiveData.postValue(radioList)
                }
            }.start()
        }
    }

    fun subscribeAll(handler: Handler){
        object : Thread() {
            override fun run() {
                try{
                    subscribeRadio()
                    subscribeRss()
                    handler.sendEmptyMessage(0x000)
                }
                catch(e:Exception){
                    handler.sendEmptyMessage(0x001)
                }
            }
        }.start()
    }

    fun subscribeRss(){
        val db = XmlDatabase.getDb(context)
        val mDao=db.xmlDao()
        try{
            mDao.insert(xmlData)
        }catch (e:Exception){ }
        XmlDatabase.closeDb()
        XmlHttpHelper().addToNet(xmlData)
    }

    fun subscribeRadio(){
        RadioHttpHelper().addToNet(radioListCache)
        val db = RadioDatabase.getDb(context)
        val mDao=db.radioDao()
        mDao.insertAll(radioListCache)
        Log.d(TAG,radioListCache[0].toString())
        RadioDatabase.closeDb()
    }
}
