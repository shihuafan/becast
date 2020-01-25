package com.example.becast.playpage.play

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.becast.R
import com.example.becast.service.MediaHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_dialog_wait_list.view.*

class WaitListBottomSheetDialog(
    owner: LifecycleOwner,
    context: Context
){
    init {
        val view = View.inflate(context, R.layout.bottom_sheet_dialog_wait_list, null)
        val bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(view)
        try {
            val parent = view.parent as ViewGroup
            parent.setBackgroundResource(android.R.color.transparent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        bottomSheetDialog.show()

        view.list_wait_dialog.layoutManager = LinearLayoutManager(context)
        view.list_wait_dialog.adapter=WaitDialogAdapter(MediaHelper().getBinder()?.getLiveData()?.value!!)
        MediaHelper().getBinder()?.getLiveData()?.observe(owner, Observer{
            view.list_wait_dialog.adapter?.notifyDataSetChanged()
        })



    }
}