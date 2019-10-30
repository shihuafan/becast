package com.example.becast.playpage.play

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.becast.R
import com.example.becast.data.ShareData
import com.example.becast.data.radioDb.RadioData
import com.example.becast.data.user.UserData
import com.example.becast.service.RadioService
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_dialog_sleep.view.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog_wait_list.view.*


class WaitListBottomSheetDialog(owner:LifecycleOwner,context: Context,mBinder: RadioService.LocalBinder){
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
        view.list_wait_dialog.adapter=WaitDialogAdapter(context,
            mBinder.getLiveData().value!!,mBinder)
        mBinder.getLiveData().observe(owner, Observer{
            view.list_wait_dialog.adapter?.notifyDataSetChanged()
        })



    }
}