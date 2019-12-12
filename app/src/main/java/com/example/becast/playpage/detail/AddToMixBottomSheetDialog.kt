package com.example.becast.playpage.detail

import android.content.Context
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.becast.R
import com.example.becast.data.mix.MixData
import com.example.becast.data.mix.MixDatabase
import com.example.becast.data.mix.MixDatabaseHelper
import com.example.becast.data.radioDb.RadioData
import com.example.becast.data.radioDb.RadioDatabase
import com.example.becast.playpage.play.SleepBottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_dialog_add_item.view.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog_add_to_mix.view.*
import kotlinx.android.synthetic.main.frag_love.view.*


class AddToMixBottomSheetDialog(context: Context, handler: Handler) {

    private val list : MutableList<MixData> = mutableListOf()

    init {

        val view = View.inflate(context, R.layout.bottom_sheet_dialog_add_to_mix, null)
        val bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(view)
        try {
            val parent = view.parent as ViewGroup
            parent.setBackgroundResource(android.R.color.transparent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val db = Room.databaseBuilder<MixDatabase>(context, MixDatabase::class.java, "mix_db")
            .allowMainThreadQueries()
            .build()
        val mDao=db.mixDao()
        val list=mDao.getAll()
        db.close()

        view.list_mix.layoutManager=LinearLayoutManager(context)
        view.list_mix.adapter=AddToMixAdapter(context, list, handler ,bottomSheetDialog)


        bottomSheetDialog.show()


    }
}