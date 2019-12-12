package com.example.becast.data

import com.example.becast.data.radioDb.RadioData

data class RecommendData (
    var title:String?=null,
    var author:String?=null,
    var other:String?=null,
    var content:MutableList<Part> = mutableListOf()
)
data class Part(
    var text:String?=null,
    var image:String?=null,
    var radio:Radio?=null

)
data class Radio(
    var title:String,
    var duration:String,
    var link:String,
    var image_uri:String,
    var rss_image_uri:String,
    var radio_uri:String,
    var pub_date:String,
    var up_date:String,
    var description:String,
    var rss_uri:String,
    var rss_title:String,
    var love_time:String,
    var history_time:String,
    var wait_time:String,
    var progress:String,
    var mix:String
)