package com.example.becast.login_signup.register

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.becast.R
import com.example.becast.data.Becast
import kotlinx.android.synthetic.main.frag_register.*
import kotlinx.android.synthetic.main.frag_register.view.*

class RegisterFragment :Fragment(), View.OnClickListener {

    private val registerViewModel= RegisterViewModel()
    val mHandler=Handler{
        when(it.what){
            Becast.SIGN_UP_SUCCESS->{
                Toast.makeText(context,"注册成功",Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            }
            Becast.SIGN_UP_FAIL->{
                Toast.makeText(context,"注册失败",Toast.LENGTH_SHORT).show()
                onResume()
            }
            Becast.NET_ERROR->{
                Toast.makeText(context,"网络错误",Toast.LENGTH_SHORT).show()
                onResume()
            }
        }
        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_register, container, false)

        view.layout_register.setOnClickListener(this)
        view.btn_register_register.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when(v!!.id){
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
