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
import com.example.becast.data.Becast
import com.example.becast.data.radio.RadioData
import com.example.becast.playpage.detail.DetailFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.frag_add_from_xml.*
import kotlinx.android.synthetic.main.frag_add_from_xml.view.*

class FromXmlFragment : Fragment(), View.OnClickListener {

    private lateinit var fromXmlViewModel: FromXmlViewModel
    private lateinit var v: View
    private lateinit var url: String
    private val mHandler : Handler = Handler{
        when(it.what){
            Becast.OPEN_DETAIL_FRAGMENT ->{
                fragmentManager!!.beginTransaction()
                    .replace(R.id.layout_add_from_xml, DetailFragment(it.obj as RadioData))
                    .addToBackStack(null)
                    .commit()
            }
            Becast.FILE_ERROR->{
                Toast.makeText(context,"xml文档解析出错",Toast.LENGTH_SHORT).show()
            }
            Becast.NET_ERROR->{
                Toast.makeText(context,"网络连接失败",Toast.LENGTH_SHORT).show()
            }

        }
        false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.frag_add_from_xml, container, false)
        url=arguments!!.getString("url") as String
        fromXmlViewModel= context?.let { FromXmlViewModel(it,url,mHandler) }!!

        v.list_add_from_xml.layoutManager = LinearLayoutManager(context)
        v.list_add_from_xml.adapter = context?.let {
            FromXmlAdapter(fromXmlViewModel.radioListLiveData.value!!, mHandler) }

        Glide.with(context!!)
            .load(resources.getDrawable(R.drawable.loading_gif,null))
            .into(v.image_add_from_xml_loading)

        //更新列表
        fromXmlViewModel.radioListLiveData.observe(this, Observer{
            v.list_add_from_xml.adapter?.notifyDataSetChanged()
        })
        fromXmlViewModel.xmlDataLiveData.observe(this, Observer {
            layout_add_from_xml_loading.visibility=View.GONE
            try{
                Glide.with(this)
                    .load(fromXmlViewModel.xmlDataLiveData.value!!.imageUrl)
                    .apply(RequestOptions.overrideOf(100,100))
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
                    .into(image_add_from_xml)
                v.text_add_from_xml.text=fromXmlViewModel.xmlDataLiveData.value!!.title
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