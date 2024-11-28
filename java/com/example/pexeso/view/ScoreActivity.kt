package com.example.pexeso.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pexeso.database.ScoreDatabase
import com.example.pexeso.databinding.ActivityScoreBinding
import kotlinx.coroutines.launch

/**
 * ScoreActivity is responsible for displaying the player's scores in a RecyclerView.
 * It fetches scores from the database and shows them in a list.
 */

class ScoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScoreBinding
    private val scoreDao by lazy { ScoreDatabase.getDatabase(this).scoreDao() }

    /**
     * Initializes the activity, sets up the RecyclerView, and fetches scores from the database.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.scoreRecyclerView.layoutManager = LinearLayoutManager(this)
        val scoreAdapter = ScoreAdapter()
        binding.scoreRecyclerView.adapter = scoreAdapter
        // Collect scores from the database using coroutines and update the RecyclerView
        lifecycle.coroutineScope.launch {
            scoreDao.getAllScores().collect() {
                scoreAdapter.submitList(it)
            }
        }

        // Set up a click listener for the "Back" button to navigate back to MainActivity
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}