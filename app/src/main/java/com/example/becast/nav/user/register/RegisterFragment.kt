package com.example.becast.nav.user.register

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.becast.R
import com.example.becast.main.MainActivity
import kotlinx.android.synthetic.main.frag_register.*
import kotlinx.android.synthetic.main.frag_register.view.*
import org.greenrobot.eventbus.EventBus
import java.util.*

class RegisterFragment :Fragment(), View.OnClickListener {

    private val registerViewModel=RegisterViewModel()
    val mHandler=Handler{
        when(it.what){
            0x001->{
                Toast.makeText(context,"successful",Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            }
            0x002->{
                Toast.makeText(context,"fail",Toast.LENGTH_SHORT).show()
                onResume()
            }
        }
        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_register, container, false)

        view.layout_register.setOnClickListener(this)
        view.btn_register_back.setOnClickListener(this)
        view.btn_register_register.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_register_back->{
                activity?.onBackPressed()
            }
            R.id.btn_register_register->{

                val id=edit_register_id.text.toString()
                val code=edit_register_code.text.toString()
                val password=edit_register_password.text.toString()
                if(id=="" || password=="" || code==""){
                    Toast.makeText(context,"id和密码不能为空",Toast.LENGTH_SHORT).show()
                }
                else{
                    registerViewModel.login(id,code,password,mHandler)
                }
            }
        }
    }
}
