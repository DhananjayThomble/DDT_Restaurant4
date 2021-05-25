package com.dhananjay.ddtrestaurant.activity

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.room.Room
import com.dhananjay.ddtrestaurant.extra.MenuDatabase
import com.dhananjay.ddtrestaurant.extra.MenuEntity
import com.dhananjay.ddtrestaurant.R
import com.google.gson.Gson

class CartActivity : AppCompatActivity() {
    lateinit var listMenu : ListView
     var arrayMenu = listOf<MenuEntity>()
    lateinit var list1 : ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        list1 = findViewById(R.id.listMenu)
        val gson = Gson()

       arrayMenu = DBAsyncTask(this).execute().get()
        val json = gson.toJson(arrayMenu)
        Log.d("test1","$json")
//        val jsArray : JsonArray = JsonArray(arrayMenu)
//        Log.d("test1","cart - ${arrayMenu[1]}")
//        for (i in arrayMenu.indices) {
//            val recipe = recipeList[i]
//            listItems[i] = recipe.title
//        }
        var adpt = ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,arrayMenu)

        list1.adapter = adpt

//        val db = Room.databaseBuilder(this, MenuDatabase::class.java, "MenuDb").build()

//        Log.d("test1",menuCart.toString())

    }
    class DBAsyncTask(val context: Context) : AsyncTask<Void, Void, List<MenuEntity>>() {


        override fun doInBackground(vararg p0: Void?): List<MenuEntity> {
            val db = Room.databaseBuilder(context, MenuDatabase::class.java, "MenuDb").build()

            db.close()

//            Log.d("test1","from cart - ${menuCart.toString()}")
            return db.menuDao().getAll()
        }

    }
}