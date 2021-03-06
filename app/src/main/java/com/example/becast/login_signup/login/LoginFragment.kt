package com.example.becast.login_signup.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.becast.R
import com.example.becast.data.Becast
import com.example.becast.main.MainActivity
import com.example.becast.login_signup.sign_up.SignUpFragment
import kotlinx.android.synthetic.main.frag_login.*
import kotlinx.android.synthetic.main.frag_login.view.*
import org.greenrobot.eventbus.EventBus

class LoginFragment :Fragment(), View.OnClickListener {
    private lateinit var v:View
    private val loginViewModel= LoginViewModel()
    private var flag:Int=0
    val mHandler=Handler{
        when(it.what){
            Becast.LOGIN_SUCCESS->{
                Toast.makeText(context,"登录成功",Toast.LENGTH_SHORT).show()
            }
            Becast.LOGIN_FAIL->{
                Toast.makeText(context,"账号不存在或密码错误",Toast.LENGTH_SHORT).show()
                onResume()
            }
            Becast.NET_ERROR->{
                Toast.makeText(context,"网络错误",Toast.LENGTH_SHORT).show()
                onResume()
            }
            Becast.LOADING_SUCCESS->{
                flag++
                if(flag==2){
                    startActivity(Intent(activity, MainActivity::class.java))
                }
            }
        }
        false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.frag_login, container, false)
        EventBus.getDefault().post("close")

        v.layout_login.setOnClickListener(this)
        v.btn_login_login.setOnClickListener(this)
        v.btn_login_sign_up.setOnClickListener(this)

        return v
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_login_login->{
                val phone=edit_login_phone.text.toString()
                val password=edit_login_password.text.toString()
                if(phone == "" || password == "" ){
                    Toast.makeText(context,"手机号和密码不能为空",Toast.LENGTH_SHORT).show()
                }
                else{
                    val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(activity!!.window.decorView.windowToken, 0)
                    context?.let { loginViewModel.login(phone,password,mHandler, it) }
                }
            }
            R.id.btn_login_sign_up->{
                fragmentManager!!.beginTransaction()
                    .replace(R.id.layout_main_all, SignUpFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}
