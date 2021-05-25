package com.dhananjay.ddtrestaurant.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dhananjay.ddtrestaurant.R

import com.dhananjay.ddtrestaurant.extra.RestaurantEntity

import com.squareup.picasso.Picasso

class DashboardRecyclerAdapterFav(val context: Context, val itemList: List<RestaurantEntity>) :
    RecyclerView.Adapter<DashboardRecyclerAdapterFav.DashboardViewHolder>() {
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
        holder.txtBookName.text = rest.resName
        holder.txRestPrice.text = rest.resCostForOne
        holder.txtRestRating.text = rest.resRating
        //holder.imgBookImage.setImageResource(book.bookImage)
        Picasso.get().load(rest.resImageUrl).error(R.drawable.ic_baseline_person_24)
            .into(holder.imgRestImage)

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


}

