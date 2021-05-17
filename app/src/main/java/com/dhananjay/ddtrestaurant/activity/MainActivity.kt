package com.dhananjay.ddtrestaurant.activity

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dhananjay.ddtrestaurant.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPrefUser : SharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE)

        Log.d("test1", sharedPrefUser.getString("name", false.toString()).toString())

//        logout()


    }

    fun logout(){
        val sharedPrefLogin = getSharedPreferences("Login", MODE_PRIVATE)
        sharedPrefLogin.edit().putBoolean("isLoggedIn",false).apply()
    }

}