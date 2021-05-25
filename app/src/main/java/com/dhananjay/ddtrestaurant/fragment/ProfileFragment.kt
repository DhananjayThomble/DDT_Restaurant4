package com.dhananjay.ddtrestaurant.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dhananjay.ddtrestaurant.R


class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)

        var txtName : TextView = view.findViewById(R.id.txtName)
        var txtPhone : TextView = view.findViewById(R.id.txtPhone)
        var txtEmail : TextView = view.findViewById(R.id.txtEmail)
        var txtAddress : TextView = view.findViewById(R.id.txtAddress)

        val sharedPrefUser  = view.context.getSharedPreferences("User", Context.MODE_PRIVATE)
        txtName.text = sharedPrefUser.getString("name","")
        txtEmail.text = sharedPrefUser.getString("email","")
        txtPhone.text = sharedPrefUser.getString("mobile","")
        txtAddress.text = sharedPrefUser.getString("address","")

        return  view
    }


}