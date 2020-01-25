package com.example.becast.data.comment

import com.google.gson.annotations.SerializedName

data class CommentData(
  var uid:String?=null,
  @SerializedName("create_time")
  var createTime:Long=0,
  var comment:String="",
  @SerializedName("xml_url")
  var xmlUrl:String="",
  @SerializedName("radio_url")
  var radioUrl:String="",
  @SerializedName("start_time")
  var startTime:Int=0,
  @SerializedName("end_time")
  var endTime:Int=0,
  @SerializedName("xml_title")
  var xmlTitle:String="",
  var title:String=""
)