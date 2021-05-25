package com.dhananjay.ddtrestaurant.extra

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [MenuEntity::class], version = 1)
abstract class MenuDatabase: RoomDatabase() {

    abstract fun menuDao(): MenuDao

}