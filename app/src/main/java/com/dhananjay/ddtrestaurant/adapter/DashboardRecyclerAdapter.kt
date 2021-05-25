package com.dhananjay.ddtrestaurant.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.dhananjay.ddtrestaurant.*
import com.dhananjay.ddtrestaurant.extra.ResId
import com.dhananjay.ddtrestaurant.extra.RestDatabase
import com.dhananjay.ddtrestaurant.extra.Restaurant
import com.dhananjay.ddtrestaurant.extra.RestaurantEntity
import com.dhananjay.ddtrestaurant.fragment.MenuFragment

import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty

class DashboardRecyclerAdapter(val context: Context, val itemList: ArrayList<Restaurant>) :
    RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {
    //    var view2 : View = TODO()
//    lateinit var rest: Restaurant
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_dashboard_single_row, parent, false)
//        view2 = view

        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val rest = itemList[position]
        holder.txtBookName.text = rest.rName
        holder.txRestPrice.text = rest.rCostForOne
        holder.txtRestRating.text = rest.rRating
        //holder.imgBookImage.setImageResource(book.bookImage)
        Picasso.get().load(rest.rImageUrl).error(R.drawable.ic_baseline_person_24)
            .into(holder.imgRestImage)

        //            Set Favourite icon
        val resEntity = RestaurantEntity(
            res_id = rest.rId.toInt(),
            resName = rest.rName,
            resCostForOne = rest.rCostForOne,
            resImageUrl = rest.rImageUrl,
            resRating = rest.rRating
        )
        val checkFav = DBAsyncTask(context, resEntity, 1).execute()
        val isFav = checkFav.get()
        if (isFav) {
//                Toasty.info(context, "Removed from Favourites", Toast.LENGTH_LONG).show()
            holder.imgFavourite.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {

//                Toasty.info(context, "Add to Favourites", Toast.LENGTH_LONG).show()
            holder.imgFavourite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
//        End.............


        holder.llContent.setOnClickListener {
//            val intent = Intent(context, DescriptionActivity::class.java)
//            intent.putExtra("rest_id", rest.rId )
//            context.startActivity(intent)
//            Toasty.normal(context, "", Toast.LENGTH_LONG).show()
//            Log.d("test1","Clicked from holder - ${rest.rId} & ResName = ${rest.rName}")
            val temp = ResId()
            temp.setResId(rest.rId)

//            val myActivity : MainActivity = MainActivity()
            val intent: Intent = Intent(context, MenuFragment::class.java)
            intent.putExtra("resId", rest.rId)
            intent.putExtra("resName",rest.rName)



            context.startActivity(intent)
        }

        holder.imgFavourite.setOnClickListener {

            val resEntity = RestaurantEntity(
                res_id = rest.rId.toInt(),
                resName = rest.rName,
                resCostForOne = rest.rCostForOne,
                resImageUrl = rest.rImageUrl,
                resRating = rest.rRating
            )

//            Log.d("test1","clicked from adpter")
//            Log.d("test1","R = $resEntity")
            val checkFav = DBAsyncTask(context, resEntity, 1).execute()
            val isFav = checkFav.get()
            if (isFav) {
                Toasty.info(context, "Removed from Favourites", Toast.LENGTH_LONG).show()
                holder.imgFavourite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            } else {

                Toasty.info(context, "Add to Favourites", Toast.LENGTH_LONG).show()
                holder.imgFavourite.setImageResource(R.drawable.ic_baseline_favorite_24)
            }

//
            if (!DBAsyncTask(
                    context,
                    resEntity,
                    1
                ).execute().get()
            ) {

                val async =
                    DBAsyncTask(context, resEntity, 2).execute()
                val result = async.get()
                if (result) {
//                    Toasty.success(context, "Restaurant Added Successfully", Toast.LENGTH_LONG)
//                        .show()
                    holder.imgFavourite.setImageResource(R.drawable.ic_baseline_favorite_24)


                } else {
//                    Toasty.error(context, "some error occurred", Toast.LENGTH_LONG).show()
                }
            } else {

                val async = DBAsyncTask(context, resEntity, 3).execute()
                val result = async.get()

                if (result) {
//                    Toasty.info(context, "Restaurant removed.", Toast.LENGTH_LONG).show()
                    holder.imgFavourite.setImageResource(R.drawable.ic_baseline_favorite_border_24)

                } else {
//                    Toasty.error(context, "some error occurred", Toast.LENGTH_LONG).show()
                }

            }

        }

//        For Storing info in db



    }


    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtBookName: TextView = view.findViewById(R.id.txtRestName)

        //        val txtRestAuthor: TextView = view.findViewById(R.id.txtRestAuthor)
        val txRestPrice: TextView = view.findViewById(R.id.txtRestPrice)
        val txtRestRating: TextView = view.findViewById(R.id.txtRestRating)
        val imgRestImage: ImageView = view.findViewById(R.id.imgRestImage)
        val llContent: LinearLayout = view.findViewById(R.id.llContent)
        val imgFavourite: ImageView = view.findViewById(R.id.imgFavourite)
    }

    class DBAsyncTask(val context: Context, val restEntity: RestaurantEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        /*
        Mode 1 -> Check DB if the book is favourite or not
        Mode 2 -> Save the book into DB as favourite
        Mode 3 -> Remove the favourite book
        * */

        val db = Room.databaseBuilder(context, RestDatabase::class.java, "RestDb").build()

        override fun doInBackground(vararg p0: Void?): Boolean {

            when (mode) {

                1 -> {

                    // Check DB if the book is favourite or not
                    val rest: RestaurantEntity? =
                        db.restDao().getRestById(restEntity.res_id.toString())
//                    Log.d("test1","from 1 - ${rest.toString()} resEntity = ${restEntity.res_id}")
                    db.close()
                    return rest != null

                }

                2 -> {

                    // Save the book into DB as favourite
                    db.restDao().insertRest(restEntity)
//                    Log.d("test1","from 2 - saved ,resEntity = ${restEntity.res_id}")

                    db.close()
                    return true

                }

                3 -> {

                    // Remove the favourite book
                    db.restDao().deleteRest(restEntity)
//                    Log.d("test1","from 3 - delete, resEntity = ${restEntity.res_id}")

                    db.close()
                    return true

                }
            }
            return false
        }

    }

}

