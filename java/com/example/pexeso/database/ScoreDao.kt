package com.example.pexeso.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.ABORT
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) interface for interacting with the `score` table in the database.
 * Provides methods for inserting, deleting, and querying scores.
 */
@Dao
interface ScoreDao {

    /**
     * Inserts a single `Score` object into the database. If there is a conflict (e.g., the score already exists),
     * the operation will be aborted.
     *
     * @param score The score object to be inserted.
     */
    @Insert(onConflict = ABORT)
    fun addScore (score: Score)
    /**
     * Inserts multiple `Score` objects into the database in one batch.
     * The operation will abort if any conflict occurs.
     *
     * @param scores The score objects to be inserted.
     */
    @Insert
    fun addScores (vararg scores: Score)
    /**
     * Deletes a specific `Score` object from the database.
     *
     * @param score The score object to be deleted.
     * @return The number of rows affected by the delete operation.
     */
    @Delete
    fun deleteScore (score: Score) : Int
    /**
     * Retrieves all scores from the database, ordered by score and time (ascending).
     * The result is returned as a `Flow` that allows observing updates to the list of scores.
     *
     * @return A `Flow` of a list of all `Score` objects.
     */
    @Query("SELECT * FROM score ORDER BY score, time")
    fun getAllScores(): Flow<List<Score>>
    /**
     * Retrieves all scores for a specific player (by their name) from the database.
     * The result is returned as a `Flow` that allows observing updates to the player's scores.
     *
     * @param name The name of the player whose scores are to be retrieved.
     * @return A `Flow` of a list of `Score` objects for the given player.
     */
    @Query ("SELECT * FROM score where name = :name")
    fun getPlayerScore (name : String) : Flow<List<Score>>
    /**
     * Deletes all scores for a specific player (by their name) from the database.
     *
     * @param name The name of the player whose scores are to be deleted.
     */
    @Query ("DELETE FROM score where name = :name")
    fun deletePlayerScore (name : String)
    /**
     * Deletes all scores from the `score` table in the database.
     */
    @Query ("DELETE FROM score")
    fun deleteAllScore ()
}