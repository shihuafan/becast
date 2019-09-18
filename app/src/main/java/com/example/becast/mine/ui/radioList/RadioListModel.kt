package com.example.becast.mine.ui.radioList

import com.example.becast.data.radioListDb.RadioListData
import com.example.becast.unit.data.radioDb.RadioData

class RadioListModel {
    lateinit var radioListData: RadioListData
    var list : MutableList<RadioData> = mutableListOf()
}