package com.dhananjay.ddtrestaurant.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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

class ForgotPasswordFragment : Fragment() {

    lateinit var etMobile: EditText
    lateinit var etEmail: EditText
    lateinit var btnSubmit: Button
    lateinit var myActivity: Context
    lateinit var reqQueue: RequestQueue

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_forgot_password, container, false)
        etEmail = view.findViewById(R.id.etEmail)
        etMobile = view.findViewById(R.id.etMobile)
        btnSubmit = view.findViewById(R.id.btnNext)
        myActivity = view.context
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        btnSubmit.setOnClickListener {

            val mobile = etMobile.text.toString()
            val email = etEmail.text.toString()

            if (mobile.isNotBlank() && email.isNotBlank()) {

                reqQueue = Volley.newRequestQueue(myActivity)
                sendPostRequest(mobile, email)


            } else {
                Toasty.warning(myActivity, "Check Your Mobile and Email", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun sendPostRequest(mobile: String, email: String) {
        val url = "http://13.235.250.119/v2/forgot_password/fetch_result"
        val jsonParams = JSONObject()
//        Log.d("test1", "$mobile $password")
        jsonParams.put("mobile_number", mobile)
        jsonParams.put("email", email)

        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonReq =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                    try {
                        val obj = it.getJSONObject("data")
                        Log.d("test1", obj.toString())
                        val isSuccess = obj.getString("success").toBoolean()
//                        If user authenticated successfully then It returns TRUE.
                        if (isSuccess) {

//                            Stores data in sharedPref
                            val sharedPrefUser: SharedPreferences =
                                myActivity.getSharedPreferences("userForgot", Context.MODE_PRIVATE)
//                            val nEmail = obj.getJSONObject("data").getString("email").toString()
//                            val nMobile =
//                                obj.getJSONObject("data").getString("mobile_number").toString()

                            sharedPrefUser.edit().putString("email", email).apply()
                            sharedPrefUser.edit().putString("mobile", mobile).apply()

// Check 1st or more attempt
                            if (obj.getString("first_try").toBoolean()) {
//                                Req for first time
                                val transaction =
                                    activity!!.supportFragmentManager.beginTransaction()
                                transaction.replace(R.id.frame, ResetPasswordFragment())
//            transaction.disallowAddToBackStack()
//                                transaction.addToBackStack("1")
                                transaction.commit()
                            } else {
                                Log.d("test1","second called")
//                                Req for other times
                                val transaction =
                                    activity!!.supportFragmentManager.beginTransaction()
                                transaction.replace(R.id.frame, ResetPasswordFragment())
//            transaction.disallowAddToBackStack()
//                                transaction.addToBackStack("1")
                                transaction.commit()
                            }

                        } else {
//                            When user enters wrong credentials
                            Toasty.error(
                                myActivity,
                                "Mobile or Email is not match!",
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