package com.dhananjay.ddtrestaurant.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dhananjay.ddtrestaurant.R
import com.dhananjay.ddtrestaurant.util.ConnectionManager
import es.dmoral.toasty.Toasty
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit

class ResetPasswordFragment : Fragment() {

    lateinit var txtCounDown : TextView
    lateinit var countDownTimer : CountDownTimer
    lateinit var myActivity : Context
    lateinit var etMobile : EditText
    lateinit var etEmail : EditText
    lateinit var etOtp : EditText
    lateinit var btnSubmit : Button
    lateinit var btnResend : Button
    lateinit var reqQueue: RequestQueue
    lateinit var etPassword : EditText
    lateinit var etPasswordConfirm : EditText
    lateinit var imgPassTick : ImageView
    lateinit var imgPassTick2 : ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reset_password, container, false)
        myActivity = view.context
        txtCounDown = view.findViewById<View>(R.id.tv_coundown) as TextView
        countDownTimer()

        etEmail = view.findViewById(R.id.etEmail)
        etMobile = view.findViewById(R.id.etMobile)
        etOtp = view.findViewById(R.id.etOtp)
        btnResend = view.findViewById(R.id.btnResend)
        btnSubmit = view.findViewById(R.id.btnSubmit)
        etPassword = view.findViewById(R.id.etPassword)
        etPasswordConfirm = view.findViewById(R.id.etPasswordConfirm)
        btnResend.isEnabled = false
        imgPassTick = view.findViewById(R.id.imgPasswordTick)
        imgPassTick2 = view.findViewById(R.id.imgPasswordConfTick)

        imgPassTick.visibility = View.INVISIBLE
        imgPassTick2.visibility = View.INVISIBLE


        val sharedPrefUser : SharedPreferences = myActivity.getSharedPreferences("userForgot", Context.MODE_PRIVATE)
        etMobile.setText(sharedPrefUser.getString("mobile",""))
        etEmail.setText(sharedPrefUser.getString("email",""))
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        reqQueue = Volley.newRequestQueue(myActivity)


        btnSubmit.setOnClickListener {
            val mobile = etMobile.text.toString()
            val otp = etOtp.text.toString()
            val password = etPassword.text.toString()
            val passwordConfirm = etPasswordConfirm.text.toString()


            if ( otp.length == 4 && if(password == passwordConfirm)  password.length>3 else false ){


                sendPostRequest(mobile,password,otp)
            }else{
                Toasty.warning(myActivity,"Please Check Your Inputs",Toast.LENGTH_LONG).show()
            }
        }
        etOtp.setOnClickListener {
            if ( etPassword.text.toString() == etPasswordConfirm.text.toString() && etPassword.text.toString().length>3 ){
                Log.d("test1","after pass input")
                imgPassTick.visibility = View.VISIBLE
                imgPassTick2.visibility = View.VISIBLE
            }
        }

    }

    private fun countDownTimer() {
        countDownTimer = object : CountDownTimer(1000 * 60 * 3600 * 24, 1000) {
            override fun onTick(l: Long) {
                val text = String.format(
                    Locale.getDefault(), "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(l) % 60,
                    TimeUnit.MILLISECONDS.toSeconds(l) % 60
                )
                txtCounDown.setText(text)
            }

            override fun onFinish() {
                txtCounDown.setText("00:00")
            }
        }
        countDownTimer.start()
    }

    fun sendPostRequest(mobile: String, password: String,otp:String) {
        val url = "http://13.235.250.119/v2/reset_password/fetch_result"
        val jsonParams = JSONObject()
//        Log.d("test1", "$mobile $password")
        jsonParams.put("mobile_number", mobile)
        jsonParams.put("password", password)
        jsonParams.put("otp", otp)

        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonReq =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                    try {
                        val obj = it.getJSONObject("data")
//                        Log.d("test1", obj.toString())
                        val isSuccess = obj.getString("success").toBoolean()
                        Log.d("test1","from ResetPasswordFrag - $obj")
//                        If user authenticated successfully then It returns TRUE.
                        if (isSuccess) {

                            Toasty.success(myActivity,"Password changed Successfully.",Toast.LENGTH_LONG).show()

                        } else {
//                            When user enters wrong credentials
                            Toasty.error(
                                myActivity,
                                "OTP is Invalid!",
                                Toast.LENGTH_LONG
                            ).show()
//
                        }
//
                    } catch (err: Exception) {
                        Log.d("test1", "error occ1 ${err.toString()}")
                        Toasty.error(myActivity, "Something is wrong! Try Again", Toast.LENGTH_LONG)
                            .show()
                    }

//                    val isSuccess = it.getBoolean("success")


                }, Response.ErrorListener {
                    Log.d("test1", "$it")
                    Toasty.error(myActivity, "Something is wrong! Try Again", Toast.LENGTH_LONG)
                        .show()
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


}