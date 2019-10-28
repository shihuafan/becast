package com.example.becast.nav.follow

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.becast.R
import com.example.becast.channel.ChannelFragment
import com.example.becast.data.rss.RssData
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.frag_follow.view.*
import org.greenrobot.eventbus.EventBus

class FollowFragment(private val mBinder: RadioService.LocalBinder):Fragment(), View.OnClickListener {

    private lateinit var followViewModel:FollowViewModel
    private val mHandler= Handler{
        when(it.what){
            0x001->{
                fragmentManager!!.beginTransaction()
                    .hide(this)
                    .add(R.id.layout_main_top, ChannelFragment(it.obj as RssData,mBinder))
                    .addToBackStack(null)
                    .commit()
            }
        }
        false
    }

    override fun onResume() {
        super.onResume()
        followViewModel.getList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_follow, container, false)
        EventBus.getDefault().post("close")
        followViewModel= context?.let { FollowViewModel(it) }!!

        view.list_follow.layoutManager = GridLayoutManager(context,3)
        view.list_follow.adapter = context?.let {
            FollowAdapter(it, followViewModel.followModelLiveData.value!!, mHandler)
        }
        followViewModel.followModelLiveData.observe(this, Observer{
            view.list_follow.adapter?.notifyDataSetChanged()
        })

        view.btn_follow_back.setOnClickListener(this)
        view.layout_follow.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_follow_back->{
                activity?.onBackPressed()
            }
        }
    }
}