package com.example.becast.nav.love

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.becast.R
import kotlinx.android.synthetic.main.dialog_add_mix.view.*

class AddMixDialog(context: Context,loveViewModel: LoveViewModel) {

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_add_mix, null);

        val builder =  AlertDialog.Builder(context)
        builder.setView(view)
        builder.setCancelable(false)
        val alert = builder.create()
        alert.show()


        view.btn_add_mix_del.setOnClickListener { alert.dismiss() }
        view.btn_add_mix_yes.setOnClickListener {
            val name=view.edit_add_mix_name.text.toString()
            if(name!=""){
                loveViewModel.createMix(name)
                alert.dismiss()
            }
        }



    }
}