package com.example.becast.nav.user

import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.becast.R
import com.example.becast.data.Becast
import com.example.becast.data.UserData
import com.example.becast.main.MainActivity
import kotlinx.android.synthetic.main.frag_info.view.*
import org.greenrobot.eventbus.EventBus


class UserFragment  : Fragment(), View.OnClickListener {


    private val userViewModel=UserViewModel()
    private val mHandler=Handler{
        if(it.what==Becast.RESTART_APP){
            startActivity(Intent(activity,MainActivity::class.java))
            activity?.finish()
        }
        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_info, container, false)
        EventBus.getDefault().post("close")

        view.layout_info_sign_up.setOnClickListener(this)
        view.btn_info_back.setOnClickListener(this)
        view.text_info_uid.text=UserData.uid
        return view
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_info_back->{
                activity?.onBackPressed()
            }
            R.id.layout_info_sign_up->{
//                    val intent =  Intent(Intent.ACTION_PICK)
//                    intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*")
//                    startActivityForResult(intent, 0x123)
                context?.let {
                    UserData.clearAll(it)
                    userViewModel.deleteAll(it,mHandler)
                }
            }
        }
    }
}