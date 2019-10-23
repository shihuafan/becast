package com.example.becast.playpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.becast.R
import com.example.becast.data.radioDb.RadioData
import com.example.becast.playpage.play.PlayPageFragment
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.frag_detail.*
import kotlinx.android.synthetic.main.frag_detail.view.*

class DetailFragment(private val radioData: RadioData,private val mBinder: RadioService.LocalBinder):Fragment(), View.OnClickListener {

    private lateinit var v:View
    private lateinit var detailViewModel:DetailViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.frag_detail, container, false)
        detailViewModel= context?.let { DetailViewModel(it,radioData) }!!

        Glide.with(context!!)
            .load(radioData.imageUri)
            .apply(RequestOptions.overrideOf(100,100))
            .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
            .into(v.image_detail_show)
        v.text_detail_name.text=radioData.title
        v.btn_detail_back.setOnClickListener(this)
        v.btn_detail_love.setOnClickListener(this)
        v.btn_detail_play.setOnClickListener(this)
        v.btn_detail_rss.setOnClickListener(this)
        return v
    }

    override fun onClick(v: View?) {
       when(v?.id){
           R.id.btn_detail_back->{

           }
           R.id.btn_detail_love->{
                detailViewModel.changeLove()
           }
           R.id.btn_detail_play->{
               mBinder.playRadio(radioData)
               fragmentManager!!.beginTransaction()
                   .replace(R.id.layout_detail, PlayPageFragment(mBinder))
                   .addToBackStack(null)
                   .commit()
           }
           R.id.btn_detail_rss->{

           }
       }

    }
}