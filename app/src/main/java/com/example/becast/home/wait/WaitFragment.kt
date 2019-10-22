package com.example.becast.home.wait

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.becast.R
import com.example.becast.home.unit.RadioAdapter
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.frag_wait.view.*

class WaitFragment(private var mBinder: RadioService.LocalBinder) : Fragment(){
    private lateinit var waitViewModel: WaitViewModel
    val mHandler=Handler{
        false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.frag_wait, container, false)

        waitViewModel= context?.let { WaitViewModel(it) }!!

        view.list_wait.layoutManager = LinearLayoutManager(context)
        view.list_wait.adapter = context?.let {
            RadioAdapter(it,waitViewModel.waitModelLiveData.value!!, mHandler)
        }

        //更新列表
        waitViewModel.waitModelLiveData.observe(this, Observer{
            view.list_wait.adapter?.notifyDataSetChanged()
        })

        return view
    }
}