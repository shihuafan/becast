package com.example.becast.more.search

import android.content.Context
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.becast.R
import com.example.becast.data.xml.XmlData

class SearchAdapter (private val context: Context, private val mData : MutableList<XmlData>, private val handler: Handler)
    : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(context)
            .load(mData[position].imageUrl)
            .apply(RequestOptions.overrideOf(100,100))
            .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
            .into(holder.imageItemShow)
        holder.textItemName.text=mData[position].title
        holder.textItemUrl.text=mData[position].xmlUrl
        holder.btnItem.setOnClickListener {
            val msg=Message()
            msg.what=0x103
            msg.obj= mData[position].xmlUrl
            handler.sendMessage(msg)
        }
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val imageItemShow:ImageView=view.findViewById(R.id.image_item_search)
        val textItemName:TextView= view.findViewById(R.id.text_item_title)
        val textItemUrl:TextView= view.findViewById(R.id.text_item_url)
        val btnItem : Button = view.findViewById(R.id.btn_item_search)
    }
}

