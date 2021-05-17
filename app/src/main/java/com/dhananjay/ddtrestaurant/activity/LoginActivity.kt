package com.dhananjay.ddtrestaurant.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.dhananjay.ddtrestaurant.fragment.LoginFragment
import com.dhananjay.ddtrestaurant.R
import es.dmoral.toasty.Toasty
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {


    lateinit var sharedPrefLogin: SharedPreferences
    lateinit var intentToMain : Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        sharedPrefLogin = getSharedPreferences("Login", Context.MODE_PRIVATE)
//        val isLoggedIn = sharedPrefLogin.getBoolean("isLoggedIn", false)

        intentToMain = Intent(this, MainActivity::class.java)
        if ( sharedPrefLogin.getBoolean("isLoggedIn",false) ){
//            User is logged in
            startActivity(intentToMain)
            finish()
        }
//        User is not logged in:-


        val fg : FragmentTransaction = supportFragmentManager.beginTransaction()
        fg.replace(R.id.frame, LoginFragment())
        fg.commit()







    }



}