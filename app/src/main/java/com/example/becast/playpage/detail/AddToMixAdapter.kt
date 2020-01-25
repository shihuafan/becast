package com.example.becast.playpage.detail

import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.becast.R
import com.example.becast.data.mix.MixData
import com.google.android.material.bottomsheet.BottomSheetDialog

class AddToMixAdapter(
    private val mData: List<MixData>,
    private val handler: Handler,
    private val bottomSheetDialog: BottomSheetDialog
)
    : RecyclerView.Adapter<AddToMixAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_love, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textItemTitle.text=mData[position].mix
        holder.btnItem.setOnClickListener {
            val msg= Message()
            msg.what=0x003
            msg.obj=mData[position]
            handler.sendMessage(msg)
            bottomSheetDialog.dismiss()
        }

    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val btnItem : Button = view.findViewById(R.id.btn_item_love)
        val textItemTitle:TextView= view.findViewById(R.id.text_item_love)
    }
}

