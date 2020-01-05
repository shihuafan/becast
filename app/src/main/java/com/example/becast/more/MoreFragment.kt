package com.example.becast.more

import android.Manifest
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.becast.R
import com.example.becast.more.from_opml.FromFileFragment
import com.example.becast.more.from_xml.FromXmlFragment
import com.example.becast.more.search.SearchFragment
import com.example.becast.service.RadioService
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.frag_more.*
import kotlinx.android.synthetic.main.frag_more.view.*
import java.util.regex.Pattern


class MoreFragment : Fragment(), View.OnClickListener{

    private lateinit var v: View
    private lateinit var mBinder: RadioService.LocalBinder

    private val mHandler= Handler{
        when(it.what){
            0x103->{
                val fromXmlFragment=FromXmlFragment()
                val bundle=Bundle()
                bundle.putBinder("Binder",mBinder)
                bundle.putString("url",it.obj as String)
                fromXmlFragment.arguments=bundle
                childFragmentManager.beginTransaction()
                    .add(R.id.layout_more, fromXmlFragment)
                    // .addToBackStack(null)
                    .commit()
                this.v.layout_more_content.visibility=View.GONE
            }
        }
        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v= inflater.inflate(R.layout.frag_more, container, false)
        mBinder= arguments!!.getBinder("Binder") as RadioService.LocalBinder
        v.layout_more.setOnClickListener(this)
        v.btn_more_back.setOnClickListener(this)
        v.btn_more_opml.setOnClickListener(this)

        v.edit_more.setOnEditorActionListener { _, actionId, _ ->
            if(actionId==EditorInfo.IME_ACTION_SEARCH){
                val imm = activity!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(activity!!.window.decorView.windowToken, 0)
                val content=edit_more.text.toString()
                val regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))" + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)"
                val pat = Pattern.compile(regex.trim())
                val mat = pat.matcher(content.trim())
                if (mat.matches()) {
                    val fromXmlFragment=FromXmlFragment()
                    val bundle=Bundle()
                    bundle.putBinder("Binder",mBinder)
                    bundle.putString("url",content)
                    fromXmlFragment.arguments=bundle
                    childFragmentManager.beginTransaction()
                        .add(R.id.layout_more, fromXmlFragment)
                       // .addToBackStack(null)
                        .commit()
                    this.v.layout_more_content.visibility=View.GONE
                }
                else{
                    val bundle=Bundle()
                    bundle.putBinder("Binder",mBinder)
                    bundle.putString("content",content)
                    val searchFragment = SearchFragment(mHandler)
                    searchFragment.arguments=bundle
                    childFragmentManager.beginTransaction()
                        .add(R.id.layout_more_show, searchFragment)
                       // .addToBackStack(null)
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
                val permissionsStorage = arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    context?.let {
                        if (ActivityCompat.checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(permissionsStorage, 1)
                        }
                        else{
                            openFromFileFragment()
                        }
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1) {
            for (grant in grantResults) {
                if(grant != PackageManager.PERMISSION_GRANTED){
                    Snackbar.make(this.v,"无法获取本地数据",Snackbar.LENGTH_SHORT).show()
                    return
                }
            }
            openFromFileFragment()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun openFromFileFragment(){
        val imm = activity!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity!!.window.decorView.windowToken, 0)
        val bundle=Bundle()
        bundle.putBinder("Binder",mBinder)
        val fromFileFragment= FromFileFragment()
        fromFileFragment.arguments=bundle
        childFragmentManager.beginTransaction()
            .replace(R.id.layout_more,fromFileFragment)
            .commit()
        this.v.layout_more_content.visibility=View.GONE
    }
}