package com.example.becast.more.from_opml

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
import com.example.becast.R
import com.example.becast.data.Becast
import com.example.becast.more.from_xml.FromXmlFragment
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.frag_opml.view.*

class FromOpmlFragment : Fragment() {

    private lateinit var fromOpmlViewModel: FromOpmlViewModel
    private lateinit var v: View

    private val mHandler : Handler = Handler{

        when(it.what){
            Becast.OPEN_XML_FRAGMENT ->{
                val fromXmlFragment= FromXmlFragment()
                val bundle=Bundle()
                bundle.putString("url",it.obj as String)
                fromXmlFragment.arguments=bundle
                fragmentManager!!.beginTransaction()
                    .hide(this)
                    .add(R.id.layout_more, fromXmlFragment)
                    .addToBackStack(null)
                    .commit()
            }
            Becast.FILE_ERROR->{
                Toast.makeText(context,"文件解析加载异常",Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            }
        }
        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.frag_opml, container, false)
        val path=arguments!!.getString("path") as String
        fromOpmlViewModel=FromOpmlViewModel(path, mHandler)

        Glide.with(context!!)
            .load(resources.getDrawable(R.drawable.loading_gif,null))
            .into(v.image_opml_loading)
        v.text_opml_file.text=path.substring( path.lastIndexOf("/") + 1)
        v.list_opml.layoutManager=LinearLayoutManager(context)
        v.list_opml.adapter=  OpmlAdapter(fromOpmlViewModel.listLiveData.value!!,mHandler)
        fromOpmlViewModel.listLiveData.observe(this, Observer {
            if(fromOpmlViewModel.listLiveData.value!!.size>0){
                v.layout_opml_loading.visibility=View.GONE
            }
            v.list_opml.adapter?.notifyDataSetChanged()
        })

        return v
    }

}