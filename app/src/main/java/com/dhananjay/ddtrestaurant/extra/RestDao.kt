package com.dhananjay.ddtrestaurant.extra

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query



@Dao
interface RestDao {

    @Insert
    fun insertRest(bookEntity: RestaurantEntity)

    @Delete
    fun deleteRest(bookEntity: RestaurantEntity)

    @Query("SELECT * FROM RestDb")
    fun getAllRest(): List<RestaurantEntity>

    @Query("SELECT * FROM RestDb WHERE res_id = :restId")
    fun getRestById(restId: String): RestaurantEntity
}

