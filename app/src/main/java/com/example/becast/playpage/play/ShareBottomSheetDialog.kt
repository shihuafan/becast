package com.example.becast.playpage.play

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.becast.R
import com.example.becast.data.ShareData
import com.example.becast.data.radioDb.RadioData
import com.example.becast.data.user.UserData
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_dialog_sleep.view.*


class ShareBottomSheetDialog(context: Context, shareData: ShareData?, radioData: RadioData?) {
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

        if(radioData!=null){
            Toast.makeText(context,"radioData",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context,"shareData",Toast.LENGTH_SHORT).show()
        }




    }
}