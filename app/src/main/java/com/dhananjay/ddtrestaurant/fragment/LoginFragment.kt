package com.dhananjay.ddtrestaurant.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dhananjay.ddtrestaurant.R
import com.dhananjay.ddtrestaurant.activity.LoginActivity
import com.dhananjay.ddtrestaurant.activity.MainActivity
import com.dhananjay.ddtrestaurant.util.ConnectionManager
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import org.json.JSONObject
import java.lang.Exception
import java.util.regex.Pattern


class LoginFragment : Fragment() {
    lateinit var etMobile: EditText
    lateinit var etPassword: EditText
    lateinit var txtForgotPass : TextView
    lateinit var txtRegister : TextView
    lateinit var gson: Gson
    lateinit var reqQueue: RequestQueue
    lateinit var intentToMain : Intent
    lateinit var btnLogin : Button
    lateinit var sharedPrefLogin: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for activity as Context fragment

        val view = inflater.inflate(R.layout.fragment_login, container, false)

        btnLogin = view.findViewById(R.id.btnLogin2)
        etMobile = view.findViewById(R.id.etMobile)
        etPassword = view.findViewById(R.id.etPassWord)
        btnLogin = view.findViewById(R.id.btnLogin2)
        txtForgotPass = view.findViewById(R.id.txtForgotPassword)
        txtRegister = view.findViewById(R.id.txtSignUp)
        gson = Gson()
        val context = activity as Context
        intentToMain = Intent(activity as Context, MainActivity::class.java)
        sharedPrefLogin = context.getSharedPreferences("Login", Context.MODE_PRIVATE)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Toasty.info(activity as Context,"Created",Toast.LENGTH_LONG).show()
        val con = activity as Context
        btnLogin.setOnClickListener {
            val mobile = etMobile.text.toString()
            val password = etPassword.text.toString()
            if (isValidMobile(mobile) && isValidPassword(password)) {
                //                Toast.makeText(activity as Context, etUserName.text.toString(), Toast.LENGTH_LONG).show()
                reqQueue = Volley.newRequestQueue(activity as Context)
                sendPostRequest(mobile, password,activity as Context)
            }
        }

        txtRegister.setOnClickListener{
//            Open Register Fragment & closes current fragment
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame, RegisterUserFragment())
//            transaction.disallowAddToBackStack()
            transaction.addToBackStack("1")
            transaction.commit()
        }
        txtForgotPass.setOnClickListener{
            val intent = Intent(view.context,RegisterUserFragment::class.java)
            startActivity(intent)
        }

    }



     fun isValidMobile(mobile: String): Boolean {
        if (!Pattern.matches("[a-zA-Z.]+", mobile) && mobile.length == 10 ) {
            return true
        } else {
            Toasty.warning(activity as Context, "invalid mobile number!", Toast.LENGTH_LONG).show()
        }
        return false
    }

     fun isValidPassword(password: String): Boolean {
        if (password.length in 6..19) {
            return true
        } else {
            Toasty.warning(activity as Context, "password length should be between 6 and 20", Toast.LENGTH_LONG)
                .show()
            return false
        }

    }

    fun sendPostRequest(mobile: String, password: String, context: Context) {
        val url = "http://13.235.250.119/v2/login/fetch_result/"
        val jsonParams = JSONObject()
//        Log.d("test1", "$mobile $password")
        jsonParams.put("mobile_number", mobile)
        jsonParams.put("password", password)

        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonReq =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
//                TODO handle retrieved data


//                    Log.d("test1", "$it, obj - $obj")
                    try {
                        val obj = it.getJSONObject("data")
                        val isSuccess = obj.getString("success").toBoolean()
//                        If user authenticated successfully then It returns TRUE.
                        if (isSuccess) {
    //                        Data fatched successfully
// set 'isLoggedIn' variable to TRUE, which initially set to FALSE.
                            sharedPrefLogin.edit().putBoolean("isLoggedIn", true).apply()

//Create Private User variable for storing user data. retrieved from json object. .
      val sharedPrefUser: SharedPreferences =
                                context.getSharedPreferences("User", Context.MODE_PRIVATE)

                            val nName = obj.getJSONObject("data").getString("name").toString()
                            val nEmail = obj.getJSONObject("data").getString("email").toString()
                            val nMobile =
                                obj.getJSONObject("data").getString("mobile_number").toString()
                            val nAdd = obj.getJSONObject("data").getString("address").toString()

                            sharedPrefUser.getBoolean("isUser", true)
                            sharedPrefUser.edit().putString("name", nName).apply()
                            sharedPrefUser.edit().putString("email", nEmail).apply()
                            sharedPrefUser.edit().putString("mobile", nMobile).apply()
                            sharedPrefUser.edit().putString("address", nAdd).apply()



                            startActivity(intentToMain)

                        //                        val personObj = it.getJSONObject("l")
//                        val mMobile = obj.
//                        Log.d("test1",mMobile)
//                        Log.d("test1","2")
                        } else{
//                            When user enters wrong credentials
                            Toasty.error(context,"Username or Password is not match!",Toast.LENGTH_LONG).show()
//                            Todo create auto dialog for forgot pass prompt
                        }
//                        Log.d("test1", it.getString("success"))
//                        Log.d("test1", obj.getJSONObject("data").getString("name").toString())
                    } catch (err: Exception) {
                        Log.d("test1", "error occ1")
                    }

//                    val isSuccess = it.getBoolean("success")


                }, Response.ErrorListener {
                    Log.d("test1", "$it")
                    Toasty.error(context,"Something is wrong! Try Again",Toast.LENGTH_LONG).show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val header = HashMap<String, String>()
                        header["Content-type"] = "application/json"
                        header["token"] = "dd5244b2c47bee"
                        return header
                    }
                }

            reqQueue.add(jsonReq)

        }


    }

//
}