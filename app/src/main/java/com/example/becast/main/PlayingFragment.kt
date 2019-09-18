package com.example.becast.main

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.becast.R
import kotlinx.android.synthetic.main.frag_playing.view.*

open class PlayingFragment : Fragment(), View.OnClickListener {


    private lateinit var v:View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.frag_playing, container, false)
        v=view
        return view
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_playing_page->{

            }
            R.id.btn_playing_pause->{
            }
            R.id.btn_playing_list->{

            }
        }
    }

}