package com.dhananjay.ddtrestaurant.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.dhananjay.ddtrestaurant.adapter.DashboardRecyclerAdapterFav
import com.dhananjay.ddtrestaurant.R
import com.dhananjay.ddtrestaurant.extra.RestDatabase
import com.dhananjay.ddtrestaurant.extra.RestaurantEntity

class FavouriteFragment : Fragment() {
    lateinit var recyclerFavourite: RecyclerView
//    lateinit var progressLayout: RelativeLayout
//    lateinit var progressBar: ProgressBar
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: DashboardRecyclerAdapterFav

    var dbBookList = listOf<RestaurantEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)

        setHasOptionsMenu(true)

        recyclerFavourite = view.findViewById(R.id.recyclerDashboard)
//        progressLayout = view.findViewById(R.id.progressLayout)
//        progressBar = view.findViewById(R.id.progressBar)

        layoutManager = GridLayoutManager(activity as Context, 1)

        dbBookList = RetrieveFavourites(activity as Context).execute().get()

        if (activity != null) {
//            progressLayout.visibility = View.GONE
            recyclerAdapter = DashboardRecyclerAdapterFav(activity as Context, dbBookList)
            recyclerFavourite.adapter = recyclerAdapter
            recyclerFavourite.layoutManager = layoutManager
        }

        return view

    }
    class RetrieveFavourites(val context: Context) : AsyncTask<Void, Void, List<RestaurantEntity>>() {

        override fun doInBackground(vararg p0: Void?): List<RestaurantEntity> {
            val db = Room.databaseBuilder(context, RestDatabase::class.java, "RestDb").build()
            Log.d("test1","from fav - ${db.restDao().getAllRest()}")
            return db.restDao().getAllRest()
        }

    }

}