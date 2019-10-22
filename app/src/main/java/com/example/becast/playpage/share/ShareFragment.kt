package com.example.becast.playpage.share

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.becast.R
import com.example.becast.data.ShareData
import com.example.becast.data.radioDb.RadioData
import kotlinx.android.synthetic.main.frag_share.view.*

class ShareFragment(private val radioData: RadioData, private val shareData: ShareData) : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.frag_share, container, false)

        context?.let {
            Glide.with(it)
                .load(radioData.imageUri)
                .into(view.image_playpage_pin)
        }

        context?.let {
            Glide.with(it)
                .load(shareData.bitmap)
                .into(view.image_playpage_pin_code)
        }

        view.text_share_title.text=radioData.title
        view.text_share_author.text=radioData.rssTitle
        return view
    }

}