package com.example.becast.more

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.becast.R
import com.example.becast.more.from_xml.FromXmlFragment
import com.example.becast.service.RadioService
import kotlinx.android.synthetic.main.frag_more.*
import kotlinx.android.synthetic.main.frag_more.view.*
import java.util.regex.Pattern


class MoreFragment(private var mBinder: RadioService.LocalBinder) : Fragment(), View.OnClickListener{

    private lateinit var v: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(com.example.becast.R.layout.frag_more, container, false)

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
                    childFragmentManager.beginTransaction()
                        .replace(R.id.layout_more_show, FromXmlFragment(mBinder,url))
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
                fragmentManager!!.beginTransaction()
                    .replace(R.id.layout_more_show, FromXmlFragment(mBinder,edit_more.text.toString()))
                    .commit()
            }
        }
    }
}