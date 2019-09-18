package com.example.becast.mine.ui

import android.content.Context
import android.net.Uri
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
import com.example.becast.unit.data.radioDb.RadioData
import com.example.becast.mine.ui.unit.DetailDialog


class RadioAdapter (private val context: Context, private val mData : MutableList<RadioData>,
                    private val handler: Handler)
    : RecyclerView.Adapter<RadioAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_radio_list, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(context)
            .load(Uri.parse(mData[position].imageUri))
            .apply(RequestOptions.bitmapTransform(RoundedCorners(10)).circleCrop())
            .into(holder.imageItemShow)

        holder.textItemTitle.text=mData[position].title
        holder.textItemDate.text=mData[position].pubDate

        holder.btnItem.setOnClickListener {
           DetailDialog(mData[position], context, handler)
        }
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val btnItem : Button = view.findViewById(R.id.btn_item_radio)
        val imageItemShow: ImageView = view.findViewById(R.id.image_item_radio_show)
        val textItemTitle:TextView= view.findViewById(R.id.text_item_radio_title)
        val textItemDate:TextView=view.findViewById(R.id.text_item_radio_describe)

    }

}

