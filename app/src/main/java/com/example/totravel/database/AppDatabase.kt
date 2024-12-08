package com.example.totravel.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 2) // Increment the version number
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
