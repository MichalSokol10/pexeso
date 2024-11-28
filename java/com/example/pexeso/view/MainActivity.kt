package com.example.pexeso.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pexeso.databinding.ActivityMainBinding

/**
 * MainActivity serves as the entry point for the app.
 * It provides options for starting a new game, viewing the scoreboard, and exiting the app.
 */

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /**
     * Initializes the activity, binds the views, and sets up click listeners for the buttons.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set a click listener for the 'New Game' button
        binding.btnNewGame.setOnClickListener {
            val intent = Intent(this, com.example.pexeso.view.NewGameActivity::class.java)
            startActivity(intent)
        }

        // Set a click listener for the 'Scoreboard' button
        binding.btnScoreboard.setOnClickListener {
            val intent = Intent(this, com.example.pexeso.view.ScoreActivity::class.java)
            startActivity(intent)
        }

        // Set a click listener for the 'Exit' button
        binding.btnExit.setOnClickListener {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            startActivity(intent)
        }
    }
}