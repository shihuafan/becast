package com.example.becast.more

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.becast.R
import com.example.becast.more.from_opml.FromFileFragment
import com.example.becast.more.from_opml.FromOpmlFragment
import com.example.becast.more.from_xml.FromXmlFragment
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.frag_more.*
import kotlinx.android.synthetic.main.frag_more.view.*
import java.util.regex.Pattern


class MoreFragment : Fragment(), View.OnClickListener{

    private lateinit var v: View
    private lateinit var mBinder: RadioService.LocalBinder
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.frag_more, container, false)
        mBinder= arguments!!.getBinder("Binder") as RadioService.LocalBinder
        v.layout_more.setOnClickListener(this)
        v.btn_more_back.setOnClickListener(this)
        v.btn_more_opml.setOnClickListener(this)

        v.edit_more.setOnEditorActionListener { _, actionId, event ->
            if(actionId==EditorInfo.IME_ACTION_SEARCH){
                val url=edit_more.text.toString()
                val regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))" + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)"
                val pat = Pattern.compile(regex.trim())
                val mat = pat.matcher(url.trim())
                if (mat.matches()) {
                    val imm = activity!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(activity!!.window.decorView.windowToken, 0)
                    val fromXmlFragment=FromXmlFragment()
                    val bundle=Bundle()
                    bundle.putBinder("Binder",mBinder)
                    bundle.putString("url",url)
                    fromXmlFragment.arguments=bundle
                    childFragmentManager.beginTransaction()
                        .replace(R.id.layout_more_show, fromXmlFragment)
                        .commit()
                }
            }
           false
        }
        return v
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_more_back->{
                activity?.onBackPressed()
            }
            R.id.btn_more_opml->{
                val imm = activity!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(activity!!.window.decorView.windowToken, 0)
                val bundle=Bundle()
                bundle.putBinder("Binder",mBinder)
                val fromFileFragment= FromFileFragment()
                fromFileFragment.arguments=bundle
                fragmentManager!!.beginTransaction()
                    .replace(R.id.layout_more, fromFileFragment)
                    .commit()
            }
        }
    }
}