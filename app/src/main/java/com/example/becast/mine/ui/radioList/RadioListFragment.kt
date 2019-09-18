package com.example.becast.mine.ui.radioList

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.becast.R
import com.example.becast.data.radioListDb.RadioListData
import com.example.becast.mine.ui.RadioAdapter
import com.example.becast.service.RadioService
import com.example.becast.unit.data.radioDb.RadioData
import com.example.becast.playpage.PlayPageFragment
import kotlinx.android.synthetic.main.frag_radio_list.*
import kotlinx.android.synthetic.main.frag_radio_list.view.*
import java.lang.Exception

class RadioListFragment(private val radioListData: RadioListData) : Fragment(), View.OnClickListener {

    private lateinit var radioListViewModel: RadioListViewModel
    private lateinit var mBinder: RadioService.LocalBinder
    private val mHandler : Handler = Handler{
        when(it.what){
            0x103 ->{
                mBinder.playRadio(it.obj as RadioData)
                fragmentManager!!.beginTransaction().replace(R.id.layout_main_all, PlayPageFragment(mBinder))
                    .addToBackStack(null)
                    .commit()
            }
        }
        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.frag_radio_list, container, false)
        mBinder= this.arguments!!.getBinder("service") as RadioService.LocalBinder
        radioListViewModel= context?.let { RadioListViewModel(it,radioListData) }!!
        view.list_radio_list.layoutManager = LinearLayoutManager(context)
        view.list_radio_list.adapter = context?.let {
            RadioAdapter(it, radioListViewModel.radioListModelLiveData.value!!.list, mHandler)
        }

        //更新列表
        radioListViewModel.radioListModelLiveData.observe(this, Observer{
            view.list_radio_list.adapter?.notifyDataSetChanged()
            try{
                Glide.with(this)
                    .asBitmap()
                    .load(radioListViewModel.radioListModel.radioListData.image)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                    .into(image_radio_list)
                view.text_radio_list.text=radioListViewModel.radioListModel.radioListData.name
            }catch (e:Exception){}
        })

        view.btn_radio_list_back.setOnClickListener(this)
        view.btn_radio_list_more.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_radio_list_back->{

            }
            R.id.btn_radio_list_more->{

            }
        }

    }
}