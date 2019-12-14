package com.example.becast.nav.history

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.becast.R
import com.example.becast.data.radio.RadioData
import com.example.becast.main.page.RadioAdapter
import com.example.becast.playpage.detail.DetailFragment
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.frag_history.view.*
import org.greenrobot.eventbus.EventBus

class HistoryFragment(private var mBinder: RadioService.LocalBinder) :Fragment(), View.OnClickListener {

    private val historyViewModel:HistoryViewModel=HistoryViewModel()

    private val mHandler : Handler = Handler{
        when(it.what){
            0x103 ->{
             //   mBinder.playRadio(it.obj as RadioData)
                fragmentManager!!.beginTransaction()
                    .hide(this)
                    .add(R.id.layout_main_all,
                        DetailFragment(
                            it.obj as RadioData,
                            mBinder
                        )
                    )
                    .addToBackStack(null)
                    .commit()
            }
        }
        false
    }

    override fun onResume() {
        super.onResume()
        context?.let { historyViewModel.getList(it) }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.frag_history, container, false)
        EventBus.getDefault().post("close")

        view.list_history.layoutManager = LinearLayoutManager(context)
        view.list_history.adapter = context?.let {
            RadioAdapter(it,historyViewModel.historyModelLiveData.value!!, mHandler)
        }

        //更新列表
        historyViewModel.historyModelLiveData.observe(this, Observer{
            view.list_history.adapter?.notifyDataSetChanged()
        })

        view.layout_history.setOnClickListener(this)
        view.btn_history_back.setOnClickListener(this)
        view.btn_history_clear.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_history_back->{
                activity?.onBackPressed()
            }
            R.id.btn_history_clear->{
                context?.let { historyViewModel.clearList(it) }
            }
        }
    }
}