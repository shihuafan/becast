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
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.SwipeRevealLayout.SwipeListener
import com.example.becast.R
import com.example.becast.data.radio.RadioData
import java.text.SimpleDateFormat
import java.util.*


class MixAdapter (private val context: Context,
                  private val mData : MutableList<RadioData>,
                  private val handler: Handler,
                  private val mixViewModel: MixViewModel)
    : RecyclerView.Adapter<MixAdapter.ViewHolder>() {

    private var flag=false
    private var swipeRevealLayout:SwipeRevealLayout ? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_mix, parent, false)
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
            if (swipeRevealLayout !=null){
                swipeRevealLayout?.close(false)
                swipeRevealLayout=null
            }else{
                val msg= Message()
                msg.what=0x001
                msg.obj=mData[position]
                handler.sendMessage(msg)
            }
        }
        holder.btnLoveAdd.setOnClickListener{
            mixViewModel.addToRadioList(mData[position])
            holder.itemLove.close(false)
        }
        holder.btnLoveCancel.setOnClickListener{
            mixViewModel.changeLove(mData[position])
            holder.itemLove.close(false)
        }

        holder.itemLove.setSwipeListener(object : SwipeListener {
            override fun onSlide(view: SwipeRevealLayout?, slideOffset: Float) {
                swipeRevealLayout?.close(false)
                swipeRevealLayout=null
            }
            override fun onClosed(view: SwipeRevealLayout?) {
                if(view==swipeRevealLayout)
                    swipeRevealLayout=null
            }
            override fun onOpened(view: SwipeRevealLayout?) { swipeRevealLayout=view }
        })

    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val btnItem : Button = view.findViewById(R.id.btn_item_mix)
        val imageItemShow: ImageView = view.findViewById(R.id.image_item_mix)
        val textItemTitle:TextView= view.findViewById(R.id.text_item_mix_name)
        val textItemDate:TextView=view.findViewById(R.id.text_item_mix_update)
        val btnLoveAdd:Button=view.findViewById(R.id.btn_item_mix_add)
        val btnLoveCancel:Button=view.findViewById(R.id.btn_item_mix_cancel)
        val itemLove: SwipeRevealLayout =view.findViewById(R.id.item_mix)

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

