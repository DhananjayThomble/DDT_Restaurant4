package com.dhananjay.ddtrestaurant.extra

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MenuDb")
data class MenuEntity(

    @PrimaryKey val menu_id: Int,
    @ColumnInfo(name = "menu_name") val menuName: String,
    @ColumnInfo(name = "cost_for_one") val menuCostForOne: String,
    @ColumnInfo(name = "res_name") val resName: String

)
