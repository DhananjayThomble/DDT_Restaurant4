package com.dhananjay.ddtrestaurant

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
//import com.internshala.bookhub.R
//import com.internshala.bookhub.activity.DescriptionActivity
//import com.internshala.bookhub.model.Book

import java.util.ArrayList

class DashboardRecyclerAdapterMenu(val itemList: ArrayList<Menu>) : RecyclerView.Adapter<DashboardRecyclerAdapterMenu.DashboardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row_menu, parent, false)

        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val rest = itemList[position]
        holder.txtMenuName.text = rest.mName
        holder.txtMenuPrice.append(rest.mCostForOne)
        val num = position + 1
        holder.txtPos.text = num.toString()
//        holder.txtRestRating.text = rest.rRating
        //holder.imgBookImage.setImageResource(book.bookImage)
//        Picasso.get().load(rest.rImageUrl).error(R.drawable.ic_baseline_person_24).into(holder.imgRestImage)

        holder.llContent.setOnClickListener {
//            val intent = Intent(context, DescriptionActivity::class.java)
//            intent.putExtra("rest_id", rest.rId )
//            context.startActivity(intent)
            Log.d("test1","From Menu - ${rest.mName} ${rest.mId}")
//            Toasty.normal(context,"Clicked - ${rest.rId}",Toast.LENGTH_LONG).show()
        }

    }

    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val txtBookName: TextView = view.findViewById(R.id.txtRestName)
//        val txtRestAuthor: TextView = view.findViewById(R.id.txtRestAuthor)
        val txtMenuName: TextView = view.findViewById(R.id.txtMenuName)
        val txtMenuPrice: TextView = view.findViewById(R.id.txtMenuPrice)
        val txtPos : TextView = view.findViewById(R.id.txtPos)
        val btnAdd: Button = view.findViewById(R.id.btnAdd)
        val llContent: LinearLayout = view.findViewById(R.id.layout2)
//        val imgFavourite : ImageView = view.findViewById(R.id.)
    }
}

