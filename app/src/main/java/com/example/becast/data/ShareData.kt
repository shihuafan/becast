package com.example.becast.data

import android.graphics.Bitmap

data class ShareData (
    var startTime:Int,
    var endTime:Int,
    var radioUri:String,
    var bitmap: Bitmap?
)
