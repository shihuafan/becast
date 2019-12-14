package com.example.becast.playpage.share

import android.graphics.Bitmap

data class ShareData (
    var startTime:Long=0,
    var endTime:Long=0,
    var xmlUrl:String="",
    var radioUrl:String="",
    var xmlImageUrl:String="",
    var radioImageUrl:String="",
    var xmlTitle:String="",
    var title:String="",
    var bitmap: Bitmap?=null
)
