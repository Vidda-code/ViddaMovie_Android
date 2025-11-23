package com.example.viddamovie.di

import android.content.Context
import androidx.room.Room
import com.example.viddamovie.data.local.AppDatabase
import com.example.viddamovie.data.local.TitleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "viddamovie_database"
        )
            .fallbackToDestructiveMigration()  // Delete & recreate if schema changes (dev only)
            .build()
    }

    @Provides
    @Singleton
    fun provideTitleDao(database: AppDatabase): TitleDao {
        return database.titleDao()
    }
}