package com.example.becast.nav.square.recommend

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.becast.R
import com.example.becast.playpage.play.PlayPageFragment
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.frag_recommend.view.*


class RecommendFragment(private val url:String,private var mBinder: RadioService.LocalBinder) : Fragment(){

    private val recommendViewModel: RecommendViewModel =
        RecommendViewModel()
    private val mHandler= Handler{
        when(it.what){
            0x001->{
                Toast.makeText(context,"1",Toast.LENGTH_SHORT).show()
//                mBinder.playRadio(RadioData(radio.title, radio.duration,radio.link, radio.image_uri,
//                    radio.rss_image_uri, radio.radio_uri, radio.pub_date, 0,radio.description,
//                    radio.rss_uri, radio.rss_title,0,0,0, 0,0
//                ))
                fragmentManager!!.beginTransaction()
                    .hide(this)
                    .add(R.id.layout_main_all, PlayPageFragment(mBinder))
                    .addToBackStack(null)
                    .commit()

            }
            0x404->{
                Toast.makeText(context,"网络异常，加载失败",Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            }
        }
        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_recommend, container, false)

        Glide.with(context!!)
            .load(resources.getDrawable(R.drawable.loading_gif,null))
            .into(view.image_recommend_loading)

        recommendViewModel.recommendModelLiveData.observe(this,Observer{
            view.text_recommend_title_header.text=it.title
            view.text_recommend_title.text=it.title
            view.text_recommend_author.text=it.author
            view.list_recommend.layoutManager= LinearLayoutManager(context)
            view.list_recommend.adapter= RecommendAdapter(
                context!!,
                it.content,
                mHandler,
                mBinder
            )
            if(it.title!=null){
                view.layout_recommend_loading.visibility=View.GONE
            }
        })

        recommendViewModel.getJson(url,mHandler)

        return view
    }
}