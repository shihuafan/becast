package com.example.becast.data.comment

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "comment_db",primaryKeys  = ["radio_url","xml_url","time"])
data class CommentData(
    @ColumnInfo (name = "comment") var comment:String="",
    @ColumnInfo (name = "time") var time:Long=0,
    @ColumnInfo (name = "radio_url") var radioUrl:String="",
    @ColumnInfo (name = "xml_url") var xnlUrl:String="",
    @ColumnInfo (name = "star_time") var startTime:Long=0,
    @ColumnInfo (name = "end_time") var endTime:Long=0
)