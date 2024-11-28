package com.example.pexeso.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Database class for accessing the `score` table.
 * It extends `RoomDatabase` and provides the abstract method `scoreDao()`
 * to access the `ScoreDao` for data operations.
 */

@Database(
    entities = [Score::class],
    version = 1
)
@TypeConverters(ScoreConverters::class)
abstract class ScoreDatabase : RoomDatabase() {
    abstract fun scoreDao(): ScoreDao

    companion object {
        @Volatile
        private var INSTANCE: ScoreDatabase? = null

        /**
         * Gets the singleton instance of the `ScoreDatabase`.
         * If the database instance does not exist, it will be created.
         *
         * @param context The context needed to build the database.
         * @return The `ScoreDatabase` instance.
         */
        fun getDatabase(context: Context): ScoreDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = buildDatabase(context)
                }
            }
            return INSTANCE!!
        }

        /**
         * Builds the `ScoreDatabase` instance using Room's databaseBuilder method.
         *
         * @param context The context used to build the database.
         * @return A new instance of `ScoreDatabase`.
         */
        private fun buildDatabase(context: Context): ScoreDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ScoreDatabase::class.java,
                "score_database"
            )
                .build()
        }
    }
}