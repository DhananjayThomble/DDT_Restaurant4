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
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dhananjay.ddtrestaurant.R
import com.dhananjay.ddtrestaurant.activity.MainActivity
import com.dhananjay.ddtrestaurant.util.ConnectionManager
import es.dmoral.toasty.Toasty
import org.json.JSONObject
import java.lang.Exception
import java.lang.reflect.Method
import java.util.regex.Pattern

 class RegisterUserFragment : Fragment() {

    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etMobile: EditText
    lateinit var etAddress: EditText
    lateinit var etPassword: EditText
    lateinit var etPasswordConf: EditText
    lateinit var txtCreateAcc: TextView
    lateinit var reqQueue: RequestQueue
    lateinit var sharedPrefLogin: SharedPreferences
   lateinit var mActivity : Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register_user, container, false)
        mActivity = view.context
        etName = view.findViewById(R.id.etName)
        etEmail = view.findViewById(R.id.etEmail)
        etMobile = view.findViewById(R.id.etMobile)
        etAddress = view.findViewById(R.id.etAddress)
        etPassword = view.findViewById(R.id.etPassword)
        etPasswordConf = view.findViewById(R.id.etPassWordConfirm)
        txtCreateAcc = view.findViewById(R.id.txt_create_account)

        sharedPrefLogin = mActivity.getSharedPreferences("Login", Context.MODE_PRIVATE)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val btnTesing : Button = view.findViewById(R.id.btnTesting)
        btnTesing.setOnClickListener {
            etName.setText("John Doe")
            etEmail.setText("john@doe.com")
            etAddress.setText("Gurugram")
            etMobile.setText("9998886666")
            etPassword.setText("123456")
            etPasswordConf.setText("123456")
        }


        txtCreateAcc.setOnClickListener {
//            Toasty.normal(view.context, "Click", Toast.LENGTH_LONG).show()
            val mName = etName.text.toString()
            val mEmail = etEmail.text.toString()
            val mMobile = etMobile.text.toString()
            val mAdd = etAddress.text.toString()
            val mPass1 = etPassword.text.toString()
            val mPass2 = etPasswordConf.text.toString()


            if (mName.isNotBlank() && mName.length > 2 && mEmail.isNotBlank() && isValidMobile(mMobile) && mAdd.isNotBlank() &&
                if (mPass1 == mPass2) isValidPassword(mPass1) else false
            ) {

                reqQueue = Volley.newRequestQueue(mActivity)
                sendPostRequest(mName, mEmail, mMobile, mAdd, mPass1, mActivity)


            } else {
                Toasty.warning(view.context, "Check Your Input", Toast.LENGTH_LONG).show()
            }

        }
    }

    fun sendPostRequest(
        name: String,
        email: String,
        mobile: String,
        address: String,
        password: String,
        context: Context
    ) {

//        Log.d("test1","$name $email $mobile $address $password")

        val url = "http://13.235.250.119/v2/register/fetch_result"
        val jsonParams = JSONObject()
//        Log.d("test1", "$mobile $password")
        jsonParams.put("name", name)
        jsonParams.put("mobile_number", mobile)
        jsonParams.put("password", password)
        jsonParams.put("address", address)
        jsonParams.put("email", email)

        if (ConnectionManager().checkConnectivity(context)) {
            val jsonReq =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
//                Response Success
                    try {
//                        Log.d("test1",it.getJSONObject("data").toString())
                        val obj = it.getJSONObject("data")
                        val isSuccess = obj.getString("success").toBoolean()
//                       If user authenticated successfully then It returns TRUE.
                        if (isSuccess) {
                            //   Data fatched successfully
                                Toasty.success(context,"User Created Successfully",Toast.LENGTH_LONG).show()

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

                            val intent = Intent(context,MainActivity::class.java)
                            startActivity(intent)
                            activity!!.finish()

                        } else {
                            Toasty.error(context, "Something is Wrong!", Toast.LENGTH_LONG).show()
                        }
                    } catch (err: Exception) {
                        Toasty.error(context, "Something is Wrong!", Toast.LENGTH_LONG).show()

                    }


                }, Response.ErrorListener {
//                Response Error
                    Toasty.error(context, "Something is Wrong!", Toast.LENGTH_LONG).show()

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

//    fun isValidName(name: String) : Boolean {
//
//    }
//    fun isValidEmail(email: String) : Boolean {}
//    fun isValidAddress(address: String) : Boolean {}

    fun isValidMobile(mobile: String): Boolean {
        if (!Pattern.matches("[a-zA-Z.]+", mobile) && mobile.length == 10) {
            return true
        } else {
            Toasty.warning(activity as Context, "invalid mobile number!", Toast.LENGTH_LONG).show()
        }
        return false
    }

    fun isValidPassword(password: String): Boolean {
        if (password.length in 4..19) {
            return true
        } else {
            Toasty.warning(
                activity as Context,
                "password length should be between 6 and 20",
                Toast.LENGTH_LONG
            )
                .show()
            return false
        }

    }


}