package com.example.becast.nav.user.login

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.becast.R
import com.example.becast.data.user.UserData
import com.example.becast.nav.user.register.RegisterFragment
import kotlinx.android.synthetic.main.frag_login.*
import kotlinx.android.synthetic.main.frag_login.view.*

class LoginFragment :Fragment(), View.OnClickListener {
    private lateinit var v:View
    private val loginViewModel= LoginViewModel()
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

    override fun onResume() {
        super.onResume()
        v.edit_login_id.hint=UserData.uid.toString()
        v.edit_login_password.hint=UserData.uid.toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.frag_login, container, false)
        v.layout_login.setOnClickListener(this)
        v.btn_login_back.setOnClickListener(this)
        v.btn_login_sign_in.setOnClickListener(this)
        v.btn_login_sign_up.setOnClickListener(this)

        return v
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_login_back->{
                activity?.onBackPressed()
            }
            R.id.btn_login_sign_in->{
                val id=edit_login_id.text.toString()
                val password=edit_login_password.text.toString()
                if(id=="" || password==""){
                    Toast.makeText(context,"id和密码不能为空",Toast.LENGTH_SHORT).show()
                }
                else{
                    context?.let { loginViewModel.login(id,password,mHandler, it) }
                }
            }
            R.id.btn_login_sign_up->{
                fragmentManager!!.beginTransaction()
                    .replace(R.id.layout_main_all, RegisterFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}
