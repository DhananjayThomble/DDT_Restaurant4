package com.dhananjay.ddtrestaurant

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

    @Query("SELECT * FROM restdb")
    fun getAllRest(): List<RestaurantEntity>

    @Query("SELECT * FROM restdb WHERE res_id = :restId")
    fun getRestById(restId: String): RestaurantEntity
}

