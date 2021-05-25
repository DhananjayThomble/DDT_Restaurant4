package com.dhananjay.ddtrestaurant.extra

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface MenuDao {

    @Insert
    fun insertMenu(menuEntity: MenuEntity)

    @Delete
    fun deleteMenu(menuEntity: MenuEntity)

    @Query("SELECT * FROM menudb")
    fun getAll(): List<MenuEntity>

    @Query("Delete from menudb")
    fun deleteAll()

    @Query("SELECT COUNT(*) from menudb")
    fun countRow() : Int

//    @Query("SELECT * FROM restdb WHERE res_id = :restId")
//    fun getRestById(restId: String): RestaurantEntity
}