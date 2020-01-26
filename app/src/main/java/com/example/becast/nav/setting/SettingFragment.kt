package com.example.becast.nav.setting

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.UserManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.becast.R
import com.example.becast.data.Becast
import com.example.becast.data.DownLoadFromNet
import com.example.becast.data.DownLoadService
import com.example.becast.data.UserData
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.frag_setting.view.*
import org.greenrobot.eventbus.EventBus

class SettingFragment :Fragment() {


    private val settingViewModel=SettingViewModel()
    private lateinit var v:View
    val mHandler=Handler{
        when(it.what){
            Becast.IS_LATEST->{
                Toast.makeText(context,"当前已是最新版本",Toast.LENGTH_SHORT).show()
                activity?.startService(Intent(activity, DownLoadService::class.java))
            }
            Becast.NOT_LATEST->{
                Toast.makeText(context,"可更新",Toast.LENGTH_SHORT).show()
            }
            Becast.IS_LATEST->{
                Toast.makeText(context,"网络错误",Toast.LENGTH_SHORT).show()
            }

        }
        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.frag_setting, container, false)

        v.layout_setting_update.setOnClickListener{settingViewModel.getLatestVersion(mHandler)}

        return v

    }

}