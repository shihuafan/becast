package com.example.becast.more.from_opml

import android.os.Handler
import android.util.Xml
import androidx.lifecycle.MutableLiveData
import org.xmlpull.v1.XmlPullParser
import java.io.FileInputStream
import java.io.InputStream


class FromOpmlViewModel(path: String, private val handler: Handler) {

    val list : MutableList<OpmlData> = mutableListOf()
    val  listLiveData: MutableLiveData<MutableList<OpmlData>> = MutableLiveData()

    init {
        listLiveData.value=list
        object :Thread(){
            override fun run() {
                super.run()
                try {
                    val input = FileInputStream(path)
                    readOpml(input)
                    listLiveData.postValue(list)
                    input.close()
                }catch (e:Exception){
                    handler.sendEmptyMessage(404)
                }
            }
        }.start()
    }

    fun readOpml(xml:InputStream){
        var opmlData: OpmlData?=null
        val parser= Xml.newPullParser()
        parser.setInput(xml,"UTF-8")
        var eventType=parser.eventType
        while (eventType!=XmlPullParser.END_DOCUMENT) {
            when(eventType){
                XmlPullParser.START_DOCUMENT->{
                   // opmlList = mutableListOf()
                }
                XmlPullParser.START_TAG->{
                    if("outline" == parser.name){
                        val text=parser.getAttributeValue(0)
                        val title=parser.getAttributeValue(1)
                        val type=parser.getAttributeValue(2)
                        val xmlUrl=parser.getAttributeValue(3)
                        val htmlUrl=parser.getAttributeValue(4)
                        opmlData= OpmlData(
                            text,
                            title,
                            type,
                            xmlUrl,
                            htmlUrl
                        )
                    }
                }
                XmlPullParser.END_TAG->{
                    if("outline" == parser.name && opmlData!=null){
                        list.add(opmlData)
                    }
                }
            }
            eventType=parser.next()
        }
    }

}