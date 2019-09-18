package com.example.becast.main

import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import com.example.becast.R
import kotlinx.android.synthetic.main.dialog_from_xml.view.*

class FromXmlDialog(context: Context,handler: Handler) {

    private val alert:AlertDialog
    private val view: View

    init {
        val inflater: LayoutInflater = LayoutInflater.from(context)        //引用自定义dialog布局
        view = inflater.inflate(R.layout.dialog_from_xml, null)

        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        builder.setCancelable(true)
        alert = builder.create()
        alert.show()

        view.btn_dialog_from_xml_yes.setOnClickListener {
            val msg=Message()
            msg.what=0x005
            msg.obj=view.edit_dialog_from_xml.text
            handler.sendMessage(msg)
            alert.dismiss()
        }
        view.btn_dialog_from_xml_cancel.setOnClickListener {
            alert.dismiss()
        }
    }


}