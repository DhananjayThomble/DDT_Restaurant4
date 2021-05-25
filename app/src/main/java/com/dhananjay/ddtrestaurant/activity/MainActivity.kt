package com.dhananjay.ddtrestaurant.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.room.Room
import com.dhananjay.ddtrestaurant.*
import com.dhananjay.ddtrestaurant.extra.MenuDatabase
import com.dhananjay.ddtrestaurant.fragment.DashboardFragment
import com.dhananjay.ddtrestaurant.fragment.FavouriteFragment
import com.dhananjay.ddtrestaurant.fragment.ProfileFragment
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    private val actionBar: ActionBar? = null
    var previousMenuItem: MenuItem? = null
    lateinit var navigationView: NavigationView
 var sharedPrefUser : SharedPreferences ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.nav_view)
//        setUpToolbar()
        initToolbar()
        initNavigationMenu()
         sharedPrefUser  = getSharedPreferences("User", Context.MODE_PRIVATE)
//        val inflatedView: View = layoutInflater.inflate(R.layout.include_drawer_header,null)
//        var txtName: TextView = inflatedView.findViewById(R.id.txtNameHeader)
//        txtName.setText("Hello")


//        Log.d("test1", sharedPrefUser.getString("name", false.toString()).toString())

//        logout()

        openDashboard()
        clearCartDb()

    }
    fun clearCartDb(){
        DBAsyncTask(this).execute()

    }
    class DBAsyncTask(val context: Context) :
        AsyncTask<Void, Void, Boolean>() {

        val db = Room.databaseBuilder(context, MenuDatabase::class.java, "MenuDb").build()

        override fun doInBackground(vararg p0: Void?): Boolean {
            db.menuDao().deleteAll()

            db.close()

            return true
        }

    }
    private fun initNavigationMenu() {
        val nav_view = findViewById<View>(R.id.nav_view) as NavigationView
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout

        val view : View = nav_view.getHeaderView(0)
        val txtHeader : TextView = view.findViewById(R.id.txtNameHeader)
        val txtMobile : TextView = view.findViewById(R.id.txtMobileHeader)
//        txtHeader.setText( sharedPrefUser!!.getString("name","") )
//        txtMobile.setText( sharedPrefUser!!.getString("mobile","") )

        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
//        drawer.setDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener {

            if (previousMenuItem != null){
                previousMenuItem?.isChecked = false
            }

            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when(it.itemId) {
                R.id.nav_home -> {
                    openDashboard()
                    drawer.closeDrawers()
                }
                R.id.nav_profile -> {
                    openProfile()
                    drawer.closeDrawers()
                }
                R.id.nav_favourite -> {
                    openFavourite()
                    drawer.closeDrawers()
                }
                R.id.nav_orderHistory -> {
                    openOrderHistory()
                    drawer.closeDrawers()
                }
                R.id.nav_faq -> {
                    openFaq()
                    drawer.closeDrawers()
                }
                R.id.nav_logout -> {
                    openLogout()
                    drawer.closeDrawers()
                }


            }

            true
        }

        // open drawer at start
//        drawer.openDrawer(GravityCompat.START)
    }

    private fun openLogout() {
        logout()
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openFaq() {
        TODO("Not yet implemented")
    }

    private fun openOrderHistory() {
        TODO("Not yet implemented")
    }

    private fun openFavourite() {
        val fragment = FavouriteFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
        supportActionBar?.title = "Favourites"
        navigationView.setCheckedItem(R.id.nav_favourite)
    }

    private fun openProfile() {
        val fragment = ProfileFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
        supportActionBar?.title = "Profile"
        navigationView.setCheckedItem(R.id.nav_profile)
    }

    private fun openDashboard() {


        val fragment = DashboardFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
        supportActionBar?.title = "Dashboard"
        navigationView.setCheckedItem(R.id.nav_home)

    }

        private fun initToolbar() {
            toolbar = findViewById<View>(R.id.toolbar) as Toolbar
            setSupportActionBar(toolbar)
           var actionBar : ActionBar? = supportActionBar
            actionBar!!.setDisplayHomeAsUpEnabled(true)
            actionBar!!.setHomeButtonEnabled(true)
            actionBar!!.title = "DDT Restaurant"
    //        Tools::java.setSystemBarColor(this)
        }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    fun logout(){
        val sharedPrefLogin = getSharedPreferences("Login", MODE_PRIVATE)
        sharedPrefLogin.edit().putBoolean("isLoggedIn",false).apply()
    }

}