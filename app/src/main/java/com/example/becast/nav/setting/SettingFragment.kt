package com.example.becast.nav.setting

import android.os.Bundle
import android.os.UserManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import com.example.becast.R
import com.example.becast.data.UserData
import kotlinx.android.synthetic.main.frag_setting.view.*
import org.greenrobot.eventbus.EventBus

class SettingFragment :Fragment(){


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_setting, container, false)

        return view

    }

}