package com.example.becast.mine.ui.addfromxml

import com.example.becast.unit.data.radioDb.RadioData
import com.example.becast.data.rss.RssData

class AddFromXmlModel {
    lateinit var rssData: RssData
    var list : MutableList<RadioData> = mutableListOf()
}