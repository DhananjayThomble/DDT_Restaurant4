package com.dhananjay.ddtrestaurant.extra

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "RestDb")
data class RestaurantEntity(

    @PrimaryKey val res_id: Int,
    @ColumnInfo(name = "res_name") val resName: String,
    @ColumnInfo(name = "res_rating") val resRating: String,
    @ColumnInfo(name = "cost_for_one") val resCostForOne: String,
    @ColumnInfo(name = "image_url") val resImageUrl: String

)
