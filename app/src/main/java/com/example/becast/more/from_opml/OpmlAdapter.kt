package com.example.becast.more.from_opml

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

class OpmlAdapter (private val mData : MutableList<OpmlData>, private val handler: Handler)
    : RecyclerView.Adapter<OpmlAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_opml, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textItemName.text=mData[position].title
        holder.textItemHtmlLink.text=mData[position].xmlUrl
        holder.btnItem.setOnClickListener {
            val msg=Message()
            msg.what=Becast.OPEN_XML_FRAGMENT
            msg.obj=mData[position].xmlUrl
            handler.sendMessage(msg)
        }
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val textItemName:TextView= view.findViewById(R.id.text_item_name)
        val btnItem : Button = view.findViewById(R.id.btn_item_opml)
        val textItemHtmlLink:TextView=view.findViewById(R.id.text_item_html_link)
    }
}

