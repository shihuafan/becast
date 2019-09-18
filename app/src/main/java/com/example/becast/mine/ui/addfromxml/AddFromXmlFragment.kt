package com.example.becast.mine.ui.addfromxml

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
import com.example.becast.service.RadioService
import com.example.becast.unit.data.radioDb.RadioData
import com.example.becast.mine.ui.RadioAdapter
import com.example.becast.playpage.PlayPageFragment
import kotlinx.android.synthetic.main.frag_add_from_xml.*
import kotlinx.android.synthetic.main.frag_add_from_xml.view.*

class AddFromXmlFragment(private val url:String) : Fragment(), View.OnClickListener {

    private lateinit var addFromXmlViewModel: AddFromXmlViewModel
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
        val view= inflater.inflate(R.layout.frag_add_from_xml, container, false)


        mBinder= this.arguments!!.getBinder("service") as RadioService.LocalBinder
        addFromXmlViewModel= context?.let { AddFromXmlViewModel(it,url) }!!

        view.list_add_from_xml.layoutManager = LinearLayoutManager(context)
        view.list_add_from_xml.adapter = context?.let { RadioAdapter(it, addFromXmlViewModel.addFromXmlModelLiveData.value!!.list,mHandler) }

        //更新列表
        addFromXmlViewModel.addFromXmlModelLiveData.observe(this, Observer{
            view.list_add_from_xml.adapter?.notifyDataSetChanged()
            try{
                Glide.with(this)
                    .asBitmap()
                    .load(addFromXmlViewModel.addFromXmlModel.rssData.imageUri)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                    .into(image_add_from_xml)
                view.text_add_from_xml.text=addFromXmlViewModel.addFromXmlModel.rssData.title
            }catch (e:Exception){}
        })

        view.btn_add_from_xml_subscribe.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_add_from_xml_subscribe->{
                addFromXmlViewModel.subscribeAll()
            }
        }
    }
}