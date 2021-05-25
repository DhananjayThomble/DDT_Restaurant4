package com.dhananjay.ddtrestaurant.adapter


import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.dhananjay.ddtrestaurant.extra.Menu
import com.dhananjay.ddtrestaurant.extra.MenuDatabase
import com.dhananjay.ddtrestaurant.extra.MenuEntity
import com.dhananjay.ddtrestaurant.R


import java.util.ArrayList

class DashboardRecyclerAdapterMenu(val itemList: ArrayList<Menu>) : RecyclerView.Adapter<DashboardRecyclerAdapterMenu.DashboardViewHolder>() {

//    lateinit var rest : Menu
    lateinit var view: View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row_menu, parent, false)

        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val rest = itemList[position]
        holder.txtMenuName.text = rest.mName
        holder.txtMenuPrice.text = "Rs. ${rest.mCostForOne}"
        val num = position + 1
        holder.txtPos.text = num.toString()

//        Log.d("test1","$num - ${rest.mName}")


        holder.btnAdd.setOnClickListener {
//            Log.d("test1","btn add clicked for ${rest.mName}")
            val menuEntity = MenuEntity(
                menu_id = rest.mId.toInt(),
                menuName = rest.mName,
                menuCostForOne = rest.mCostForOne,
                resName = rest.mResName

            )
//            fillMenuEntity()
//            fillDb()
//            Log.d("test1","from menu - ${menuEntity.toString()}")ry
            try {
                DBAsyncTask(view.context, menuEntity).execute()
                holder.btnAdd.setBackgroundColor(view.resources.getColor(R.color.yellow_500))
            } catch (ex : Exception){                Log.d("test1","Exception caught.2")
            }

        }

    }

//    fun fillDb(menuEntity: MenuEntity = fillMenuEntity()){
//
////        val db = Room.databaseBuilder(view.context, MenuDatabase::class.java, "menu1-db").build()
//
//
//    }

//    fun fillMenuEntity() : MenuEntity {
//
//        return menuEntity
//    }

    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val txtBookName: TextView = view.findViewById(R.id.txtRestName)
//        val txtRestAuthor: TextView = view.findViewById(R.id.txtRestAuthor)
        val txtMenuName: TextView = view.findViewById(R.id.txtMenuName)
        val txtMenuPrice: TextView = view.findViewById(R.id.txtMenuPrice)
        val txtPos : TextView = view.findViewById(R.id.txtPos)
        val btnAdd: Button = view.findViewById(R.id.btnAdd)
//        val btnGotoCart: Button = view.findViewById(R.id.btnProceedCart)
        val llContent: LinearLayout = view.findViewById(R.id.layout2)
//        val btnAdd : Button = view.findViewById(R.id.btnAdd)
//        val imgFavourite : ImageView = view.findViewById(R.id.)
    }
    class DBAsyncTask(val context: Context, val menuEntity: MenuEntity) :
        AsyncTask<Void, Void, Boolean>() {

        val db = Room.databaseBuilder(context, MenuDatabase::class.java, "MenuDb").build()

        override fun doInBackground(vararg p0: Void?): Boolean {
            try {
                db.menuDao().insertMenu(menuEntity)
//                    Log.d("test1","from 2 - saved ,resEntity = ${restEntity.res_id}")

                db.close()

                return true
            } catch (ex : java.lang.Exception){
                Log.d("test1","Exception caught.")
            }
            return false

        }

    }
}

