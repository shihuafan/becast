package com.example.becast.nav.square

import com.example.becast.data.mix.MixData

data class SquareData (
    var mix:MutableList<MixData> = mutableListOf(),
    var comment:MutableList<Part> = mutableListOf(),
    var recommend:MutableList<Part> = mutableListOf()
)
data class Part(
    var text:String?=null,
    var image:String?=null,
    var radio: Radio?=null
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
    var progress:String
)
