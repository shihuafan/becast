package com.example.becast.playpage.share

import com.google.gson.annotations.Expose

data class ShareData (
    @Expose
    var uid:String?=null,
    @Expose
    var createTime:String="",
    var startTime:Int=0,
    var endTime:Int=0,
    var xmlTitle:String="",
    var title:String="",
    var xmlUrl:String="",
    var radioUrl:String="",
    var xmlImageUrl:String="",
    var radioImageUrl:String="",
    var shareLink:String?=null,
    var comment:String?=null
)
