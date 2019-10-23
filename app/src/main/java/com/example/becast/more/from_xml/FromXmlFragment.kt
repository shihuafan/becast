package com.example.becast.more.from_xml

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.becast.R
import com.example.becast.service.RadioService
import com.example.becast.data.radioDb.RadioData
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
            RadioAdapter(it, fromXmlViewModel.radioListLiveData.value!!,mHandler) }

        Glide.with(context!!)
            .load(resources.getDrawable(R.drawable.loading_gif,null))
            .into(v.image_add_from_xml_loading)

        //更新列表
        fromXmlViewModel.radioListLiveData.observe(this, Observer{
            v.list_add_from_xml.adapter?.notifyDataSetChanged()
        })

        fromXmlViewModel.rssDataLiveData.observe(this, Observer {
            layout_add_from_xml_loading.visibility=View.GONE
            try{
                Glide.with(this)
                    .load(fromXmlViewModel.rssDataLiveData.value!!.imageUri)
                    .apply(RequestOptions.overrideOf(100,100))
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                    .into(image_add_from_xml)
                v.text_add_from_xml.text=fromXmlViewModel.rssDataLiveData.value!!.title
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