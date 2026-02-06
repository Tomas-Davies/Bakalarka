package com.tomdev.logopadix.dataLayer

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


/**
 * Provides access to the local database and its tables.
 */
@Database(entities = [UserSentence::class, StickerProgress::class, DailyActivity::class], version = 3)
abstract class AppDb: RoomDatabase(){
    abstract fun sentencesDao(): UserSentenceDAO
    abstract fun stickersDao(): StickerProgressDao
    abstract fun dailyActivityDao(): DailyActivityDao

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
                    .fallbackToDestructiveMigration() // TODO has to be deleted before launch, will need MANUAL MIGRATION
                    .build()
            }
            return instance as AppDb
        }
    }
}