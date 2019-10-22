package com.example.becast.channel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.becast.R
import com.example.becast.data.rss.RssData

class ChannelFragment(private val rssData: RssData):Fragment() {

    private lateinit var v:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.frag_channel, container, false)

        
        return v
    }
}