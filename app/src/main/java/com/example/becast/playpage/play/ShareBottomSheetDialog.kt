package com.example.becast.playpage.play

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.example.becast.R
import com.google.android.material.bottomsheet.BottomSheetDialog


class ShareBottomSheetDialog(context: Context,content:String) {
    init {
        val view = View.inflate(context, R.layout.bottom_sheet_dialog_share, null)
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