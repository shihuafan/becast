package com.example.becast.more.from_xml

import android.content.Context
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.becast.R
import com.example.becast.data.Becast
import com.example.becast.data.radio.RadioData
import java.text.SimpleDateFormat
import java.util.*


class FromXmlAdapter (private val context: Context, private val mData : MutableList<RadioData>,
                    private val handler: Handler)
    : RecyclerView.Adapter<FromXmlAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_more, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textItemName.text=mData[position].title
        holder.textItemTitle.text=mData[position].xmlTitle
        holder.textItemDate.text=getDateString(mData[position].upDate)

        holder.btnItem.setOnClickListener {
            val msg= Message()
            msg.what=Becast.OPEN_PLAY_PAGE_FRAGMENT
            msg.obj=mData[position]
            handler.sendMessage(msg)
        }
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val btnItem : Button = view.findViewById(R.id.btn_item_radio)
        val textItemName:TextView= view.findViewById(R.id.text_item_name)
        val textItemTitle:TextView= view.findViewById(R.id.text_item_title)
        val textItemDate:TextView=view.findViewById(R.id.text_item_update)
    }

    private fun getDateString(update:Long):String{
        val date= Date(update)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        return try {
            sdf.format(date)
        } catch (e: Exception) {
            ""
        }
    }

}

