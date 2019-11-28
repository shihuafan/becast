package com.example.becast.data

import android.graphics.Bitmap

data class ShareData (
    var startTime:Long,
    var endTime:Long,
    var radioUri:String,
    var bitmap: Bitmap?
)
