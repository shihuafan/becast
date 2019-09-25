package com.example.becast.mine.ui.radioList

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.becast.data.radioList.RadioListData
import com.example.becast.unit.data.radioDb.RadioData
import com.example.becast.unit.data.radioDb.RadioDatabase


class RadioListViewModel(context: Context,radioListData: RadioListData) {

    var list : MutableList<RadioData> = mutableListOf()
    val radioListModelLiveData: MutableLiveData<MutableList<RadioData>> = MutableLiveData()
    init {
        radioListModelLiveData.value=list
        val db= Room.databaseBuilder(context, RadioDatabase::class.java,"radio")
            .allowMainThreadQueries()
            .build()
        val mDao=db.radioDao()
        list=mDao.getRadioList(radioListData.id) as MutableList<RadioData>
        db.close()
        radioListModelLiveData.value=list
    }

}