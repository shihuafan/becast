package com.example.becast.nav.user

import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import com.example.becast.R
import com.example.becast.data.Becast
import kotlinx.android.synthetic.main.dialog_add_mix.view.btn_add_mix_del
import kotlinx.android.synthetic.main.dialog_add_mix.view.btn_add_mix_yes
import kotlinx.android.synthetic.main.dialog_change_name.view.*

class ChangeNameDialog(context: Context,handler: Handler,userViewModel:UserViewModel) {

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_change_name, null)

        val builder =  AlertDialog.Builder(context)
        builder.setView(view)
        builder.setCancelable(false)
        val alert = builder.create()
        alert.show()

        view.btn_add_mix_del.setOnClickListener { alert.dismiss() }
        view.btn_add_mix_yes.setOnClickListener {
            val name=view.edit_change_name.text.toString()
            if(name!=""){
                userViewModel.changeName(context,handler,name)
                alert.dismiss()
            }
        }



    }
}