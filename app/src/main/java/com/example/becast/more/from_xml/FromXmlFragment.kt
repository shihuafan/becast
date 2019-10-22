package com.example.becast.more.from_xml

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
import com.example.becast.data.radioDb.RadioData
import com.example.becast.more.addfromxml.FromXmlViewModel
import com.example.becast.home.unit.RadioAdapter
import com.example.becast.playpage.play.PlayPageFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.frag_add_from_xml.*
import kotlinx.android.synthetic.main.frag_add_from_xml.view.*

class FromXmlFragment(private var mBinder: RadioService.LocalBinder, private val url:String) : Fragment(), View.OnClickListener {

    private lateinit var fromXmlViewModel: FromXmlViewModel
    private lateinit var v: View

    private val mHandler : Handler = Handler{
        when(it.what){
            0x103 ->{
                mBinder.playRadio(it.obj as RadioData)
                fragmentManager!!.beginTransaction().replace(R.id.layout_main_all,
                    PlayPageFragment(mBinder)
                )
                    .addToBackStack(null)
                    .commit()
            }
        }
        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.frag_add_from_xml, container, false)

        fromXmlViewModel= context?.let { FromXmlViewModel(it,url) }!!

        v.list_add_from_xml.layoutManager = LinearLayoutManager(context)
        v.list_add_from_xml.adapter = context?.let {
            RadioAdapter(it, fromXmlViewModel.fromXmlModelLiveData.value!!.list,mHandler) }

        //更新列表
        fromXmlViewModel.fromXmlModelLiveData.observe(this, Observer{
            v.list_add_from_xml.adapter?.notifyDataSetChanged()
            try{
                Glide.with(this)
                    .asBitmap()
                    .load(fromXmlViewModel.fromXmlModel.rssData.imageUri)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                    .into(image_add_from_xml)
                v.text_add_from_xml.text=fromXmlViewModel.fromXmlModel.rssData.title
            }catch (e:Exception){}
        })

        v.layout_add_from_xml.setOnClickListener(this)
        v.btn_add_from_xml_subscribe.setOnClickListener(this)
        return v
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_add_from_xml_subscribe->{
                val handler = Handler{
                    when(it.what){
                        0x000->Snackbar.make(v, "订阅成功", Snackbar.LENGTH_SHORT).show()
                        0x001->Snackbar.make(v, "订阅失败", Snackbar.LENGTH_SHORT).show()
                    }
                    false
                }
                fromXmlViewModel.subscribeAll(handler)
            }
        }
    }
}