package com.dhananjay.ddtrestaurant

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.Menu
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dhananjay.ddtrestaurant.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView

    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var recyclerAdapter: DashboardRecyclerAdapter

    lateinit var progressLayout: RelativeLayout

    lateinit var progressBar: ProgressBar

    val restInfoList = arrayListOf<Restaurant>()

    var ratingComparator = Comparator<Restaurant>{rest1, rest2 ->

        if (rest1.rRating.compareTo(rest2.rRating, true) == 0) {
            // sort according to name if rating is same
            rest1.rName.compareTo(rest2.rName, true)
        } else {
            rest1.rRating.compareTo(rest2.rRating, true)
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        setHasOptionsMenu(true)

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)

        progressLayout = view.findViewById(R.id.progressLayout)

        progressBar = view.findViewById(R.id.progressBar)

        progressLayout.visibility = View.VISIBLE

        layoutManager = LinearLayoutManager(activity)



        val queue = Volley.newRequestQueue(activity as Context)

        val url = " http://13.235.250.119/v2/restaurants/fetch_result/"

        if (ConnectionManager().checkConnectivity(activity as Context)){
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
//                Log.d("test1","1 - ${it.getJSONObject("data")}")
                // Here we will handle the response
                try {
                    progressLayout.visibility = View.GONE
                    val success = it.getJSONObject("data").getBoolean("success")
//                    Log.d("test1","2  - ${it.toString()}")
//                    Log.d("test1"," it.toString())

                    if (success){

//                        Log.d("test1","succ")
                        val data = it.getJSONObject("data").getJSONArray("data")
//                        Log.d("test1"," 3 - $data ")
//                        Log.d("test1","data - ${it.toString()}")
                        for (i in 0 until data.length()){
                            val restJsonObject = data.getJSONObject(i)
                            val restObject = Restaurant(
                                restJsonObject.getString("id"),
                                restJsonObject.getString("name"),
                                restJsonObject.getString("rating"),
                                restJsonObject.getString("cost_for_one"),
                                restJsonObject.getString("image_url")
                            )
                            restInfoList.add(restObject)
                            recyclerAdapter = DashboardRecyclerAdapter(activity as Context, restInfoList)

                            recyclerDashboard.adapter = recyclerAdapter

                            recyclerDashboard.layoutManager = layoutManager

                        }

                    } else {
                        Toast.makeText(activity as Context, "Some Error Occurred!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    Toast.makeText(activity as Context, "Some unexpected error occurred!", Toast.LENGTH_SHORT).show()
                    Log.d("test1","Error - ${it.getJSONObject("data")}")
                }

            }, Response.ErrorListener {

                //Here we will handle the errors
                if (activity != null){
                    Toast.makeText(activity as Context, "Volley error occurred!", Toast.LENGTH_SHORT).show()
                }

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
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings"){text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }

            dialog.setNegativeButton("Exit") {text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }


        return view
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dashboard, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == R.id.action_sort){
            Collections.sort(restInfoList, ratingComparator)
            restInfoList.reverse()
        }

        recyclerAdapter.notifyDataSetChanged()

        return super.onOptionsItemSelected(item)
    }

}