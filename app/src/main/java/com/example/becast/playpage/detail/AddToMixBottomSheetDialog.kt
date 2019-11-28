package com.example.becast.playpage.detail

import android.content.Context
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import com.example.becast.R
import com.example.becast.playpage.play.SleepBottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_dialog_add_item.view.*


class AddToMixBottomSheetDialog(context: Context, handler: Handler) {
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

        bottomSheetDialog.show()


    }
}