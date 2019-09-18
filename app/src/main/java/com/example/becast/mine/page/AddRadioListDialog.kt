package com.example.becast.mine.page

import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import com.example.becast.R
import kotlinx.android.synthetic.main.dialog_from_xml.view.*

class AddRadioListDialog(context: Context,handler: Handler) {

    init {
        val inflater: LayoutInflater = LayoutInflater.from(context)        //引用自定义dialog布局
        val view = inflater.inflate(R.layout.dialog_add_radio_list, null)

        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        builder.setCancelable(true)
        val alert = builder.create()
        alert.show()

        view.btn_dialog_from_xml_yes.setOnClickListener {
            val name=view.edit_dialog_from_xml.text.toString()
            val msg=Message()
            msg.obj=name
            handler.sendMessage(msg)
            alert.dismiss()
        }

        view.btn_dialog_from_xml_cancel.setOnClickListener {
            alert.dismiss()
        }
    }
}