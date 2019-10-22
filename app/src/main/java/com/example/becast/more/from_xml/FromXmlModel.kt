package com.example.becast.more.from_xml

import com.example.becast.data.radioDb.RadioData
import com.example.becast.data.rss.RssData

class FromXmlModel {
    lateinit var rssData: RssData
    var list : MutableList<RadioData> = mutableListOf()
}