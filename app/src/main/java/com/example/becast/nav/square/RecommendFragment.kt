package com.example.becast.nav.square

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.becast.R
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.frag_recommend.view.*


class RecommendFragment(private val url:String,private var mBinder: RadioService.LocalBinder) : Fragment(){

    private val recommendViewModel:RecommendViewModel=RecommendViewModel()
    private val mHandler= Handler{
        when(it.what){
            0x001->{
                mBinder.pauseRadio()
//                mBinder.playRadio(RadioData(radio.title, radio.duration,radio.link, radio.image_uri,
//                    radio.rss_image_uri, radio.radio_uri, radio.pub_date, 0,radio.description,
//                    radio.rss_uri, radio.rss_title,0,0,0, 0,0
//                ))
            }
            0x404->{
                activity?.onBackPressed()
            }
        }
        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_recommend, container, false)

        recommendViewModel.recommendModelLiveData.observe(this,Observer{
            view.list_recommend.layoutManager= LinearLayoutManager(context)
            view.list_recommend.adapter=RecommendAdapter(context!!,it.content,mHandler)
            view.text_recommend_title_header.text=it.title
            view.text_recommend_title.text=it.title
            view.text_recommend_author.text=it.author
        })

        recommendViewModel.getJson(url,mHandler)

        return view
    }
}