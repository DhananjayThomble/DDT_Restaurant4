package com.dhananjay.ddtrestaurant.fragment


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dhananjay.ddtrestaurant.*
import com.dhananjay.ddtrestaurant.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

class MenuFragment : AppCompatActivity() {

    lateinit var recyclerDashboard: RecyclerView

    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var recyclerAdapter: DashboardRecyclerAdapterMenu

    lateinit var progressLayout: RelativeLayout

    lateinit var progressBar: ProgressBar
    lateinit var con : Context

    val bookInfoList = arrayListOf<com.dhananjay.ddtrestaurant.Menu>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_dashboard)
        con  = applicationContext

        // Inflate the layout for this fragment


//        setHasOptionsMenu(true)

        recyclerDashboard = findViewById(R.id.recyclerDashboard)

        progressLayout = findViewById(R.id.progressLayout)

        progressBar = findViewById(R.id.progressBar)

        progressLayout.visibility = View.VISIBLE

        layoutManager = LinearLayoutManager(this)
//        layoutManager = R


        val queue = Volley.newRequestQueue(this)
        val tempIntent = intent.extras!!["resId"]
        val resId = tempIntent.toString()
//        val temp = ResId()

        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$resId"

        if (ConnectionManager().checkConnectivity(this)){
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

                // Here we will handle the response
                try {
                    progressLayout.visibility = View.GONE
                    val success = it.getJSONObject("data").getBoolean("success")

                    if (success){

                        val data = it.getJSONObject("data").getJSONArray("data")
                        for (i in 0 until data.length()){
                            val bookJsonObject = data.getJSONObject(i)
//                            Log.d("test1","menu lenght = ${data.length()}")
                            val bookObject = com.dhananjay.ddtrestaurant.Menu(

                                bookJsonObject.getString("id"),
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("cost_for_one"),
                                bookJsonObject.getString("restaurant_id")

                            )
                            bookInfoList.add(bookObject)

                            recyclerAdapter = DashboardRecyclerAdapterMenu(bookInfoList)

                            recyclerDashboard.adapter = recyclerAdapter

                            recyclerDashboard.layoutManager = layoutManager

                        }

                    } else {
                        Toast.makeText(this, "Some Error Occurred!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    Toast.makeText(this, "Some unexpected error occurred!", Toast.LENGTH_SHORT).show()
                }

            }, Response.ErrorListener {

                //Here we will handle the errors
//                if (this != null){
//                    Toast.makeText(this, "Volley error occurred!", Toast.LENGTH_SHORT).show()
//                }

            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "dd5244b2c47bee"
                    return headers
                }
            }


            queue.add(jsonObjectRequest)

        } else {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings"){text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }

            dialog.setNegativeButton("Exit") {text, listener ->
                ActivityCompat.finishAffinity(this)
            }
            dialog.create()
            dialog.show()
        }

    }


//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater?.inflate(R.menu.menu_dashboard, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//        val id = item?.itemId
//        if (id == R.id.action_sort){
//            Collections.sort(bookInfoList, ratingComparator)
//            bookInfoList.reverse()
//        }
//
//        recyclerAdapter.notifyDataSetChanged()
//
//        return super.onOptionsItemSelected(item)
//    }
}
