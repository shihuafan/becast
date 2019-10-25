package com.example.becast.nav.love

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.becast.R
import com.example.becast.data.radioDb.RadioData
import com.example.becast.playpage.DetailFragment
import com.example.becast.playpage.play.PlayPageFragment
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.frag_love.view.*

class LoveFragment(private var mBinder: RadioService.LocalBinder) :Fragment(), View.OnClickListener {
    private lateinit var loveViewModel: LoveViewModel
    private val mHandler : Handler = Handler{
        when(it.what){
            0x001 ->{
                mBinder.playRadio(it.obj as RadioData)
                fragmentManager!!.beginTransaction()
                    .replace(R.id.layout_main_all, DetailFragment(it.obj as RadioData,mBinder))
                    .addToBackStack(null)
                    .commit()
            }
        }
        false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view= inflater.inflate(R.layout.frag_love, container, false)

        loveViewModel= context?.let { LoveViewModel(it) }!!

        view.list_love.layoutManager = LinearLayoutManager(context)
        view.list_love.adapter = context?.let {
            LoveAdapter(it,loveViewModel.loveModelLiveData.value!!, mHandler,loveViewModel)
        }

        //更新列表
        loveViewModel.loveModelLiveData.observe(this, Observer{
            view.list_love.adapter?.notifyDataSetChanged()
        })

        view.layout_love.setOnClickListener(this)
        view.btn_love_back.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_love_back->{
                activity?.onBackPressed()
            }
        }
    }
}