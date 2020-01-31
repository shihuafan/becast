package com.example.becast.nav.square.recommend

import android.content.Context
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.becast.R
import com.example.becast.service.MediaHelper
import com.example.becast.service.RadioService


class RecommendAdapter (private val context: Context,
                        private val mData : MutableList<Part>,
                        private val handler: Handler)
    : RecyclerView.Adapter<RecommendAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_recommend, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textItem.visibility=View.GONE
        holder.imageItem.visibility=View.GONE
        holder.layoutItem.visibility=View.GONE

        mData[position].text?.let{
                holder.textItem.visibility=View.VISIBLE
                holder.textItem.text=it
        }
        mData[position].image?.let{
                holder.imageItem.visibility=View.VISIBLE
                Glide.with(context)
                    .load(it)
                    .apply(RequestOptions.overrideOf(400,300))
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                    .into(holder.imageItem)
        }
        mData[position].radio?.let{ it ->
            holder.layoutItem.visibility=View.VISIBLE
            holder.textItemTitle.text=it.title
            Glide.with(context)
                .load(it.image_uri)
                .apply(RequestOptions.overrideOf(100,100))
                .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                .into(holder.imageItemShow)
            holder.btnItemPlay.setOnClickListener {
                MediaHelper().getPlayer()?.let {it_->
                    if(it_.pauseRadio()){
                        it.setBackgroundResource(R.drawable.pause_light)
                    }
                    else{
                        it.setBackgroundResource(R.drawable.play_light)
                    }
                }

            }
            holder.btnItem.setOnClickListener {
                val msg=Message()
                msg.what=0x001
                msg.obj=it
                handler.sendMessage(msg)
            }
        }

    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val imageItem: ImageView = view.findViewById(R.id.image_item_recommend)
        val textItem:TextView=view.findViewById(R.id.text_item_recommend)
        val layoutItem:RelativeLayout=view.findViewById(R.id.layout_item_recommend)
        val imageItemShow: ImageView = view.findViewById(R.id.image_item_recommend_show)
        val textItemTitle:TextView=view.findViewById(R.id.text_item_recommend_title)
        val btnItemPlay:TextView=view.findViewById(R.id.btn_item_recommend_radio_play)
        val btnItem:TextView=view.findViewById(R.id.btn_item_recommend_radio)

    }
}

