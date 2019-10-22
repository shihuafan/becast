package com.example.becast.nav.user.Info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.becast.R
import com.example.becast.data.user.UserData
import kotlinx.android.synthetic.main.frag_info.view.*

class InfoFragment  : Fragment(), View.OnClickListener {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_info, container, false)

        view.layout_info_sign_up.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.layout_info_sign_up->{
                context?.let { UserData.clearAll(it) }
                activity?.onBackPressed()
            }
        }
    }
}