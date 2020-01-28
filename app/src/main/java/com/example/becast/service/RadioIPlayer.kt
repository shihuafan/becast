package com.example.becast.service

import androidx.lifecycle.MutableLiveData
import com.example.becast.data.radio.RadioData

interface RadioIPlayer{

        fun playPreRadio()
        fun playNextRadio()
        fun pauseRadio():Boolean
        fun getRadioDuration():Int
        fun getRadioCurrentPosition():Int
        fun seekRadioTo( progress: Int)
        fun isRadioPlaying():Boolean
        fun isPrepared():Boolean
        fun changeRadioSpeed():Int
        fun playRadio(item: RadioData)
        fun addRadioItem(item: RadioData)
        fun addRadioItemToNext(item: RadioData)
        fun deleteRadioItem(index:Int)
        fun radioItemEmpty():Boolean
        fun getRadioItem(): RadioData
        fun getLiveData(): MutableLiveData<MutableList<RadioData>>

}