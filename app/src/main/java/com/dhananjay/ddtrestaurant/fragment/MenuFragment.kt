package com.dhananjay.ddtrestaurant.fragment


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dhananjay.ddtrestaurant.*
import com.dhananjay.ddtrestaurant.activity.CartActivity
import com.dhananjay.ddtrestaurant.adapter.DashboardRecyclerAdapterMenu
import com.dhananjay.ddtrestaurant.extra.Menu
import com.dhananjay.ddtrestaurant.extra.MenuDatabase
import com.dhananjay.ddtrestaurant.util.ConnectionManager
import es.dmoral.toasty.Toasty
import org.json.JSONException
import java.util.*
import kotlin.collections.HashMap

class MenuFragment : AppCompatActivity() {

    lateinit var recyclerDashboard: RecyclerView

    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var recyclerAdapter: DashboardRecyclerAdapterMenu

//    lateinit var progressLayout: RelativeLayout

//    lateinit var progressBar: ProgressBar
    lateinit var con : Context
    lateinit var toolbar: Toolbar

    val bookInfoList = arrayListOf<Menu>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_menu_dashboard)
        con  = applicationContext
        initToolbar()


        recyclerDashboard = findViewById(R.id.recyclerDashboard)

//        progressLayout = findViewById(R.id.progressLayout)

//        progressBar = findViewById(R.id.progressBar)
//
//        progressLayout.visibility = View.VISIBLE

        layoutManager = LinearLayoutManager(this)
//        layoutManager = R
        val btnCart : Button = findViewById(R.id.btnProceedCart)
//        val dbStatus =
//
//        val timer = object: CountDownTimer(20000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                if ( ){
//                    btnCart.isEnabled = true
//                }
//                Log.d("test1","tick")
//            }
//
//            override fun onFinish() {}
//        }
//        timer.start()


        btnCart.setOnClickListener {
            if( DBAsyncTask(this@MenuFragment).execute().get() ){
                gotoCart()
            }else{
                Toasty.warning(this,"You Have not add any Product to Cart!",Toast.LENGTH_LONG).show()
            }
//            Log.d("test1","Pressed Cart")
        }

        val queue = Volley.newRequestQueue(this)
        val tempIntent = intent.extras!!["resId"]
        val resId = tempIntent.toString()
//        val temp = ResId()

        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$resId"

        if (ConnectionManager().checkConnectivity(this)){
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

                // Here we will handle the response
                try {
//                    progressLayout.visibility = View.GONE
                    val success = it.getJSONObject("data").getBoolean("success")

                    if (success){

                        val data = it.getJSONObject("data").getJSONArray("data")
                        for (i in 0 until data.length()){
                            val bookJsonObject = data.getJSONObject(i)
//                            Log.d("test1","menu lenght = ${data.length()}")
                            val bookObject = Menu(

                                bookJsonObject.getString("id"),
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("cost_for_one"),
                                bookJsonObject.getString("restaurant_id"),
                                intent.extras!!["resName"].toString()

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
    fun gotoCart(){
        val intent = Intent(this, CartActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        Log.d("test1","hhh")
        finish()
        return true

    }

      private fun initToolbar() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val actionBar : ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeButtonEnabled(true)
        actionBar.title = intent.extras!!["resName"].toString()


    }
    class DBAsyncTask(val context: Context) :
        AsyncTask<Void, Void, Boolean>() {

        val db = Room.databaseBuilder(context, MenuDatabase::class.java, "MenuDb").build()

        override fun doInBackground(vararg p0: Void?): Boolean {
            val n = db.menuDao().countRow()
            Log.d("test1","from db - ${n.toString()}")
            db.close()

            if (n > 0)
                return true
            else
                return false


        }

    }


}
