package com.example.pexeso.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pexeso.databinding.ActivityNewGameBinding

/**
 * NewGameActivity is responsible for handling the input of the player's name
 * and starting the game once the player is ready.
 */

class NewGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewGameBinding

    /**
     * Initializes the activity, binds the layout, and sets up the button click listener.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set a click listener for the 'Start' button
        binding.btnStart.setOnClickListener {
            // Check if the player name is empty
            if (binding.editTextText.text.isEmpty()) {
                Toast.makeText(this, "Enter the player's name.",Toast.LENGTH_LONG).show()
            } else {
                val i = Intent(this, GameActivity::class.java)
                i.putExtra("playerName", binding.editTextText.text.toString())
                startActivity(i) // Start the GameActivity
            }
        }
    }
}