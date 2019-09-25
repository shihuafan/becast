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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.becast.R
import com.example.becast.data.rss.RssData
import com.example.becast.mine.ui.unit.RadioAdapter
import kotlinx.android.synthetic.main.frag_follow.view.*
import kotlinx.android.synthetic.main.frag_love.view.*

class FollowFragment:Fragment() {

    private lateinit var followViewModel:FollowViewModel
    private val mHandler= Handler{
        when(it.what){
            0x001->{

            }
        }
        false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_follow, container, false)

        followViewModel= context?.let { FollowViewModel(it) }!!

        view.list_follow.layoutManager = GridLayoutManager(context,1)
        view.list_follow.adapter = context?.let {
            FollowAdapter(it, followViewModel.followModelLiveData.value!!, mHandler)
        }
        followViewModel.followModelLiveData.observe(this, Observer{
            view.list_follow.adapter?.notifyDataSetChanged()
        })
        return view
    }
}