package com.tomdev.logopadix.dataLayer

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


/**
 * Provides access to the local database and its tables.
 */
@Database(entities = [UserSentence::class], version = 1)
abstract class AppDb: RoomDatabase(){
    abstract fun sentencesDao(): UserSentenceDAO
    companion object {
        @Volatile
        private var instance: AppDb? = null
        fun getDatabase(ctx: Context): AppDb {
            if (instance != null) {
                return instance as AppDb
            } else {
                instance = Room.databaseBuilder(
                    ctx.applicationContext,
                    AppDb::class.java,
                    "LogopadixDatabase"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance as AppDb
        }
    }
}