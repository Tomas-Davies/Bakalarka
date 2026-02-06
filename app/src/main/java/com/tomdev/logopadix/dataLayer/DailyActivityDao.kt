package com.tomdev.logopadix.dataLayer

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DailyActivityDao {
    @Query("SELECT * FROM daily_activity")
    suspend fun getAll(): List<DailyActivity>

    @Query("SELECT * FROM daily_activity WHERE date LIKE :date")
    suspend fun getByDate(date: String): DailyActivity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(activity: DailyActivity)

    @Update
    suspend fun update(activity: DailyActivity)

    @Query("SELECT COUNT(*) FROM daily_activity WHERE practiced = 1")
    suspend fun getTotalPracticedDays(): Int

    @Query("SELECT * FROM daily_activity WHERE date BETWEEN :start AND :end")
    suspend fun getWeek(start: String, end: String): List<DailyActivity>
}