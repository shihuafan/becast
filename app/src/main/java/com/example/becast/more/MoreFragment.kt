package com.example.becast.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.becast.R
import com.example.becast.more.from_xml.FromXmlFragment
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.frag_more.*
import kotlinx.android.synthetic.main.frag_more.view.*

class MoreFragment(private var mBinder: RadioService.LocalBinder) : Fragment(), View.OnClickListener {


    private lateinit var v: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.frag_more, container, false)

        v.layout_more.setOnClickListener(this)
        v.btn_more_back.setOnClickListener(this)
        v.btn_more_search.setOnClickListener(this)
        v.btn_more_opml.setOnClickListener(this)
        return v
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_more_back->{
                activity?.onBackPressed()
            }
            R.id.btn_more_search->{
                fragmentManager!!.beginTransaction()
                    .replace(R.id.layout_more_show, FromXmlFragment(mBinder,edit_more.text.toString()))
                    .commit()
            }
            R.id.btn_more_opml->{
                fragmentManager!!.beginTransaction()
                    .replace(R.id.layout_more_show, FromXmlFragment(mBinder,edit_more.text.toString()))
                    .commit()
            }
        }
    }
}