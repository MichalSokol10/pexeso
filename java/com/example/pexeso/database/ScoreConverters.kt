package com.example.pexeso.database

import androidx.room.TypeConverter
import java.util.Date

/**
 * This class contains methods for converting complex data types to and from a format that can be stored in the database.
 */

class ScoreConverters {
    /**
     * Converts a timestamp (Long) from the database into a Date object.
     *
     * @param value The timestamp value to be converted. It is nullable, meaning it can be null.
     * @return The Date corresponding to the timestamp, or null if the value is null.
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    /**
     * Converts a Date object into a timestamp (Long) to be stored in the database.
     *
     * @param date The Date object to be converted. It is nullable, meaning it can be null.
     * @return The timestamp corresponding to the Date, or null if the Date is null.
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}