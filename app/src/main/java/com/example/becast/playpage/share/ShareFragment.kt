package com.example.becast.playpage.share

 import android.content.ContentValues.TAG
 import android.os.Bundle
 import android.os.Handler
 import android.util.Log
 import android.view.LayoutInflater
 import android.view.View
 import android.view.ViewGroup
 import android.widget.Toast
 import androidx.fragment.app.Fragment
 import com.bumptech.glide.Glide
 import com.bumptech.glide.request.RequestOptions
 import com.example.becast.R
 import kotlinx.android.synthetic.main.frag_share.view.*
 import java.util.*


class ShareFragment(private val shareData: ShareData) : Fragment(), View.OnClickListener {

    lateinit var v:View
    private val shareViewModel: ShareViewModel=ShareViewModel()

    private val mHandler=Handler{

        v.layout_share_background.setBackgroundColor(it.obj as Int)

        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.frag_share, container, false)

        arguments?.get("share_data")

        context?.let {
            Glide.with(it)
                .load(shareData.xmlImageUrl)
                .apply(RequestOptions.overrideOf(300,400))
                .into(v.image_share_image)
        }
        v.text_share_xmltitle.text=shareData.xmlTitle
        v.text_share_title.text=shareData.title
        context?.let {
            Glide.with(it)
                .load(shareViewModel.createQRCodeBitmap("shihuafan"))
                .apply(RequestOptions.overrideOf(300,300))
                .into(v.image_share_qrcode)
        }

        shareViewModel.getBackgroundColor(mHandler,shareData.xmlImageUrl)
        return v
    }

    override fun onClick(v: View?) {
        when(v?.id){

        }
    }



}

