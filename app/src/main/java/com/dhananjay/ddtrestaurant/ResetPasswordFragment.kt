package com.dhananjay.ddtrestaurant

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.*
import java.util.concurrent.TimeUnit

class ResetPasswordFragment : Fragment() {

    lateinit var txtCounDown : TextView
    lateinit var countDownTimer : CountDownTimer
    lateinit var myActivity : Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reset_password, container, false)
        myActivity = view.context
        txtCounDown = view.findViewById<View>(R.id.tv_coundown) as TextView
        countDownTimer()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    private fun countDownTimer() {
        countDownTimer = object : CountDownTimer(1000 * 60 * 24, 1000) {
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



}