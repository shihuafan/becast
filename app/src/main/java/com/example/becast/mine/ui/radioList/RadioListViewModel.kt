package com.example.becast.mine.ui.radioList

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.becast.data.radioListDb.RadioListData
import com.example.becast.unit.data.radioDb.RadioData
import com.example.becast.unit.data.radioDb.RadioDatabase


class RadioListViewModel(context: Context,radioListData: RadioListData) {

    val radioListModel: RadioListModel = RadioListModel()
    val radioListModelLiveData: MutableLiveData<RadioListModel> = MutableLiveData()
    init {
        radioListModel.radioListData=radioListData
        radioListModelLiveData.value=radioListModel
        val db= Room.databaseBuilder(context, RadioDatabase::class.java,"radio")
            .allowMainThreadQueries()
            .build()
        val mDao=db.radioDao()
        radioListModel.list=mDao.getRadioList(radioListModel.radioListData.id) as MutableList<RadioData>
        db.close()
        radioListModelLiveData.postValue(radioListModel)
    }

}