package com.example.becast.playpage.play

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.becast.R
import com.example.becast.data.radio.RadioData
import com.example.becast.service.MediaHelper


class WaitDialogAdapter(private val mData: MutableList<RadioData>)
    : RecyclerView.Adapter<WaitDialogAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_wait_dialog, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textItemName.text=mData[position].title

        holder.btnItem.setOnClickListener {
            MediaHelper().getBinder()?.deleteRadioItem(position)
//            mBinder.playRadio(mData[position])
        }
        holder.btnItemDelete.setOnClickListener {
            MediaHelper().getBinder()?.deleteRadioItem(position)
        }
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val btnItem : Button = view.findViewById(R.id.btn_item_radio)
        val btnItemDelete : Button = view.findViewById(R.id.btn_item_delete)
        val textItemName:TextView= view.findViewById(R.id.text_item_name)


    }

}

