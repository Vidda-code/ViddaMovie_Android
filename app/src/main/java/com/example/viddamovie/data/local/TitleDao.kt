package com.example.viddamovie.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) for Title database operations
 *
 * iOS Equivalent: SwiftData ModelContext methods
 *
 * Purpose: Define database queries and operations
 * - Room generates implementation automatically
 * - Type-safe SQL queries
 * - Compile-time query validation
 *
 * @Dao: Marks this as a Room Data Access Object
 */
@Dao
interface TitleDao {

    @Query("SELECT * FROM saved_titles ORDER BY title ASC")
    fun getAllTitles(): Flow<List<TitleEntity>>

    @Query("SELECT * FROM saved_titles WHERE id = :titleId")
    suspend fun getTitleById(titleId: Int): TitleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTitle(title: TitleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTitles(titles: List<TitleEntity>)

    @Delete
    suspend fun deleteTitle(title: TitleEntity)

    @Query("DELETE FROM saved_titles")
    suspend fun deleteAllTitles()

    @Query("SELECT EXISTS(SELECT 1 FROM saved_titles WHERE id = :titleId)")
    suspend fun isTitleSaved(titleId: Int): Boolean

    @Query("SELECT COUNT(*) FROM saved_titles")
    fun getSavedTitlesCount(): Flow<Int>
}