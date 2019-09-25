package com.example.becast.nav.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.becast.R
import kotlinx.android.synthetic.main.frag_about.view.*

class AboutFragment:Fragment(), View.OnClickListener {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_about, container, false)

        view.layout_about.setOnClickListener(this)
        view.btn_about_back.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_about_back->{
                activity?.onBackPressed()
            }
        }
    }


}
