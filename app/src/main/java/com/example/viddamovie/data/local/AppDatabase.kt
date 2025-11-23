package com.example.viddamovie.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TitleEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun titleDao(): TitleDao
}