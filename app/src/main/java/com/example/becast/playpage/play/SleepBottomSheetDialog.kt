package com.example.becast.playpage.play

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.example.becast.R
import com.example.becast.data.UserData
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_dialog_sleep.view.*
import org.greenrobot.eventbus.EventBus


class SleepBottomSheetDialog(context: Context) {
    init {
        val view = View.inflate(context, R.layout.bottom_sheet_dialog_sleep, null)
        val bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(view)
        try {
            val parent = view.parent as ViewGroup
            parent.setBackgroundResource(android.R.color.transparent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        bottomSheetDialog.show()
        val list= mutableListOf(view.btn_sleep_close,
            view.btn_sleep_10,view.btn_sleep_30,view.btn_sleep_60)
        var delay= UserData.delay
        list[delay].setBackgroundResource(R.drawable.radius_concern_40_0xf7c325)

        for(i in 0 until list.size){
            list[i].setOnClickListener{
                //被点击，通知事件总线，在activity中使用timertask
                UserData.delay=i
                EventBus.getDefault().post("delay")
                bottomSheetDialog.dismiss()
            }
        }


    }
}