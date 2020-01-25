package com.example.becast.nav.user

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.becast.R
import com.example.becast.data.Becast
import com.example.becast.data.UserData
import com.example.becast.main.MainActivity
import kotlinx.android.synthetic.main.frag_info.view.*
import org.greenrobot.eventbus.EventBus


class UserFragment  : Fragment(), View.OnClickListener {


    private val userViewModel=UserViewModel()
    private lateinit var v:View
    private val mHandler=Handler{
        when(it.what){
            Becast.RESTART_APP->{
                startActivity(Intent(activity,MainActivity::class.java))
            }
            Becast.RESTART->{
                EventBus.getDefault().post("change_user")
                setView()
            }
        }
        false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_info, container, false)
        EventBus.getDefault().post("close")

        view.layout_info_sign_up.setOnClickListener(this)
        view.btn_info_back.setOnClickListener(this)
        view.btn_image_user.setOnClickListener(this)
        view.btn_info_name.setOnClickListener(this)
        v=view
        context?.let { userViewModel.getUserData(it,mHandler) }
        setView()
        return view
    }

    private fun setView(){
        v.text_info_uid.text=UserData.name
        v.text_user_time_sum.text=userViewModel.timeToStr(UserData.timeSum)
        v.text_user_radio_sum.text=UserData.radioSum.toString()
        v.text_user_xml_sum.text=UserData.xmlSum.toString()
        context?.let {
            Glide.with(it)
                .load(UserData.image)
                .apply(RequestOptions().error(R.drawable.default_head))
                .apply(RequestOptions().circleCrop())
                .into(v.image_user)
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_info_back->{
                activity?.onBackPressed()
            }
            R.id.layout_info_sign_up->{
                context?.let {
                    UserData.clearAll(it)
                    userViewModel.deleteAll(it,mHandler)
                }
            }
            R.id.btn_image_user->{
                val intent =  Intent(Intent.ACTION_PICK)
                intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*")
                startActivityForResult(intent, 1)
            }
            R.id.btn_info_name->{
                context?.let { ChangeNameDialog(it,mHandler,userViewModel) }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==1){
            if (resultCode == Activity.RESULT_OK) {
                val url=data!!.data
                url?.let{ crop(it) }
            }
        }else if(requestCode==2){
            if (data != null && data.extras != null) {
                val extras = data.extras
                extras?.let{
                    val bitmap: Bitmap? = extras.getParcelable("data")
                    bitmap?.let{
                        context?.let { it1 -> userViewModel.uploadImage(it1,it,mHandler) }
                    }
                }
            }
        }
    }

    private fun crop(uri: Uri) {
        val intent = Intent("com.android.camera.action.CROP")
        with(intent) {
            setDataAndType(uri, "image/*")
            putExtra("crop", "true")
            putExtra("aspectX", 1)          // aspectX aspectY 是宽高的比例
            putExtra("aspectY", 1)
            intent.putExtra("outputX", 150) // outputX outputY 是裁剪图片宽高
            intent.putExtra("outputY", 150)
            intent.putExtra("return-data", true)
        }
        startActivityForResult(intent, 2)
    }
}