package com.example.becast.nav.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.becast.R
import kotlinx.android.synthetic.main.frag_setting.view.*

class SettingFragment :Fragment(), View.OnClickListener {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_setting, container, false)

        view.layout_setting.setOnClickListener(this)
        view.btn_setting_back.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_setting_back->{
                activity?.onBackPressed()
            }
        }
    }
}