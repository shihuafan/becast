package com.example.becast.nav.love

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
import com.example.becast.data.mix.MixData


class LoveAdapter (private val context: Context,
                   private val mData : MutableList<MixData>,
                   private val handler: Handler)
    : RecyclerView.Adapter<LoveAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_love, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(position==0){
            Glide.with(context)
                .load(R.drawable.timg)
                .apply(RequestOptions.overrideOf(100,100))
                .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                .into(holder.imageItemShow)

            holder.textItemTitle.text=mData[position].mix
            holder.btnItem.setOnClickListener {
                val msg= Message()
                msg.what=0x001
                msg.obj=mData[position]
                handler.sendMessage(msg)
            }
        }
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val imageItemShow: ImageView = view.findViewById(R.id.image_item_love)
        val btnItem : Button = view.findViewById(R.id.btn_item_love)
        val textItemTitle:TextView= view.findViewById(R.id.text_item_love)
    }
}

