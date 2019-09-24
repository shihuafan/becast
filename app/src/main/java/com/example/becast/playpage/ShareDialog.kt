package com.example.becast.playpage

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.becast.R

class ShareDialog(context: Context) {


    init{
        val inflater: LayoutInflater = LayoutInflater.from(context)        //引用自定义dialog布局
        val view = inflater.inflate(R.layout.dialog_datil, null)

        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        builder.setCancelable(true)
        val alert = builder.create()
        alert.show()
    }
}
