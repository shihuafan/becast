package com.example.becast.nav.history

import android.content.Context
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.becast.R
import com.example.becast.data.Becast
import com.example.becast.data.radio.RadioData
import java.text.SimpleDateFormat
import java.util.*


class HistoryAdapter (private val context: Context, private val mData : MutableList<RadioData>,
                      private val handler: Handler)
    : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(context)
            .load(mData[position].xmlImageUrl)
            .apply(RequestOptions.overrideOf(100,100))
            .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
            .into(holder.imageItemShow)

        val duration=mData[position].duration/1000/60
        val process=mData[position].progress/1000/60
        holder.textItemName.text=mData[position].title
        holder.textItemTitle.text=mData[position].xmlTitle
        holder.textItemDate.text=getDateString(mData[position].upDate)
        holder.processItem.max=duration
        holder.processItem.progress=process
        holder.textItemProgress.text="${process}/${duration}min"
        holder.btnItem.setOnClickListener {
            val msg= Message()
            msg.what= Becast.OPEN_DETAIL_FRAGMENT
            msg.obj=mData[position]
            handler.sendMessage(msg)
        }
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val btnItem : Button = view.findViewById(R.id.btn_item_history)
        val imageItemShow: ImageView = view.findViewById(R.id.image_item_history_show)
        val textItemName:TextView= view.findViewById(R.id.text_item_name)
        val textItemTitle:TextView= view.findViewById(R.id.text_item_title)
        val textItemDate:TextView=view.findViewById(R.id.text_item_update)
        val textItemProgress:TextView=view.findViewById(R.id.text_item_progress)
        val processItem: ProgressBar =view.findViewById(R.id.progress_item_history)
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

