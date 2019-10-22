package com.example.becast.nav.love

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.example.becast.R
import com.example.becast.data.radioDb.RadioData
import java.text.SimpleDateFormat
import java.util.*




class LoveAdapter (private val context: Context,
                   private val mData : MutableList<RadioData>,
                   private val handler: Handler,
                   private val loveViewModel: LoveViewModel)
    : RecyclerView.Adapter<LoveAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_love, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


//        val fb= FBitamp()
//        val fbhandler=Handler{
//            var bitmap=fb.size(100,100,it.obj as Bitmap)
//            bitmap=fb.roundedCorner(0.2F,bitmap)
//            holder.imageItemShow.setImageBitmap(bitmap)
//            false
//        }
//        fb.getBitmap(mData[position].imageUri,fbhandler)

        holder.textItemTitle.text=mData[position].title
        holder.textItemDate.text=getDateString(mData[position].upDate)
        holder.btnItem.setOnClickListener {
       //    DetailDialog(mData[position], context, handler)
        }
        holder.btnLoveAdd.setOnClickListener{
            loveViewModel.addToRadioList(mData[position])
            holder.itemLove.close(false)
        }
        holder.btnLoveCancel.setOnClickListener{ loveViewModel.changeLove(mData[position]) }
        holder.itemLove.close(false)
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val btnItem : Button = view.findViewById(R.id.btn_item_love)
        val imageItemShow: ImageView = view.findViewById(R.id.image_item_love)
        val textItemTitle:TextView= view.findViewById(R.id.text_item_love_name)
        val textItemDate:TextView=view.findViewById(R.id.text_item_love_update)
        val btnLoveAdd:Button=view.findViewById(R.id.btn_item_love_add)
        val btnLoveCancel:Button=view.findViewById(R.id.btn_item_love_cancel)
        val itemLove: SwipeRevealLayout =view.findViewById(R.id.item_love)

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

