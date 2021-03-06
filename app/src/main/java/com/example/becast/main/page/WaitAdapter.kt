package com.example.becast.main.page

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
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.example.becast.R
import com.example.becast.data.Becast
import com.example.becast.data.radio.RadioData
import com.example.becast.service.player.RadioIPlayer
import java.text.SimpleDateFormat
import java.util.*

class WaitAdapter (private val context: Context,
                   private val mData : MutableList<RadioData>,
                   private val handler: Handler,
                   private val mBinder: RadioIPlayer
)
    : RecyclerView.Adapter<WaitAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_wait, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(context)
            .load(mData[position].xmlImageUrl)
            .apply(RequestOptions.overrideOf(100,100))
            .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
            .into(holder.imageItemShow)

        holder.textItemTitle.text=mData[position].title
        holder.textItemDate.text=getDateString(mData[position].upDate)
        holder.btnItem.setOnClickListener {
            val msg= Message()
            msg.what= Becast.OPEN_DETAIL_FRAGMENT
            msg.obj=mData[position]
            handler.sendMessage(msg)
        }

        holder.btnWaitCancel.setOnClickListener{
            mBinder.deleteRadioItem(position)
        }
        holder.itemWait.close(false)
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val btnItem : Button = view.findViewById(R.id.btn_item_wait)
        val imageItemShow: ImageView = view.findViewById(R.id.image_item_wait)
        val textItemTitle:TextView= view.findViewById(R.id.text_item_wait_name)
        val textItemDate:TextView=view.findViewById(R.id.text_item_wait_update)
        val btnWaitCancel:Button=view.findViewById(R.id.btn_item_wait_cancel)
        val itemWait: SwipeRevealLayout =view.findViewById(R.id.item_wait)

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

