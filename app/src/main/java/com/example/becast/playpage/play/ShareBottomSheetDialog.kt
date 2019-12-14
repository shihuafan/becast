package com.example.becast.playpage.play

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Message
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.becast.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_dialog_share.view.*
import okhttp3.*
import java.io.IOException


class ShareBottomSheetDialog(context: Context,content:String,bitmap: Bitmap) {
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