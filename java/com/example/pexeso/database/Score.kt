package com.example.pexeso.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

/**
 * This class represents a "score" table in the database.
 * Each instance of this class represents a record in the "score" table.
 */
@Entity (
    tableName = "score",
    indices = arrayOf(
        Index(value = arrayOf("score", "time"), unique = false),
    ))

data class Score(
    /**
     * The primary key for the "score" table..
     */
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    /**
     * The date when the score record was added.
     */
    @ColumnInfo(name="dateAdded") val date: Date,
    /**
     * The name of the player or user associated with the score.
     */
    val name: String,
    /**
     * The score value, which is an integer representing the player's score.
     */
    val score: Int,
    /**
     * The time associated with the score, likely representing the duration of the game in milliseconds.
     */
    val time: Long,
)
