package com.example.pexeso.view

import GameViewModelFactory
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pexeso.R
import com.example.pexeso.database.Score
import com.example.pexeso.database.ScoreDatabase
import com.example.pexeso.databinding.ActivityGameBinding
import com.example.pexeso.viewmodel.GameViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Date

/**
 * The GameActivity handles the game's user interface and logic.
 * It interacts with the GameViewModel to manage game state and updates the UI.
 */
class GameActivity : AppCompatActivity(), GameViewModel.GameListener {

    private lateinit var binding: ActivityGameBinding
    private lateinit var gameViewModel: GameViewModel
    private var isMatchingInProgress = false
    private var startTime: Long = 0
    private lateinit var chronometer: Chronometer
    private lateinit var playerName: TextView
    private var dialogClosed = false
    private lateinit var scoreTextView: TextView
    private val scoreDao by lazy { ScoreDatabase.getDatabase(this).scoreDao() }

    /**
     * Initializes the game activity, sets up the game view, and starts the chronometer.
     * Also initializes the cards and their click listeners.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize UI components
        playerName = findViewById(R.id.tv_jmeno_hrace)

        chronometer = findViewById(R.id.chronometer)

        scoreTextView = findViewById(R.id.tv_skore)

        // Get player name from the intent
        if (intent.extras != null) {
            playerName.text = intent.extras!!.getString("playerName").toString()
        }

        // Initialize the GameViewModel
        val factory = GameViewModelFactory(this)
        gameViewModel = ViewModelProvider(this, factory).get(GameViewModel::class.java)
            // Initialize the cards and set up click listeners
            for (position in 0 until gameViewModel.getCards().size) {
                val imageView = findViewById<ImageView>(getResourceId(position))
                imageView.setImageResource(R.drawable.cardback)
                imageView.setOnClickListener {
                    onCardClicked(position)
                }

                Log.d("CardInitialization", "Position: $position, ImagePath: ${gameViewModel.getCards()[position].imagePath}")
            }

        startChronometer()
        updateScore()

        }

    /**
     * Updates the score display with the number of moves.
     */
   private fun updateScore() {
        scoreTextView.text = gameViewModel.getNumberOfMoves().toString()
    }

    /**
     * Handles the card click event.
     * Only processes the click if no matching is in progress.
     */
    private fun onCardClicked(position: Int) {
        if (!isMatchingInProgress) {
            gameViewModel.onCardClicked(position)
        }
    }

    /**
     * Called when a card is flipped to reveal its image.
     * Updates the UI to show the card's front image.
     */
    override fun onCardFlipped(position: Int) {
        val card = gameViewModel.getCards()[position]
        val imageView = findViewById<ImageView>(getResourceId(position))

        val resourceId = resources.getIdentifier(card.imagePath, "drawable", packageName)

        imageView.setImageResource(resourceId)

        Log.d("CardFlipped", "Position: $position, ImagePath: ${card.imagePath}, ResourceId: $resourceId")
    }

    /**
     * Called when a match between two flipped cards is found.
     * Updates the score and flips the matched cards.
     */
    override fun onMatchFound() {
            updateScore()

            for (card in gameViewModel.getFlippedCards()) {
                val position = gameViewModel.getCards().indexOf(card)
                val imageView = findViewById<ImageView>(getResourceId(position))
                imageView.rotationY = 180f
        }
    }

    /**
     * Called when the flipped cards do not match.
     * Hides the cards after a delay and updates the score.
     */
    override fun onMismatch() {
            updateScore()
            isMatchingInProgress = true

            for (card in gameViewModel.getFlippedCards()) {
                val position = gameViewModel.getCards().indexOf(card)
                val imageView = findViewById<ImageView>(getResourceId(position))
                imageView.postDelayed({
                    imageView.setImageResource(R.drawable.cardback)
                    isMatchingInProgress = false
                }, 2000)
            }
    }

    /**
     * Called when the game is complete.
     * Stops the chronometer, updates the score, and shows the game result dialog.
     */
    override fun onGameComplete() {
        stopChronometer()
        updateScore()

        val playerName = playerName.text.toString()
        val moves = gameViewModel.getNumberOfMoves()
        val elapsedTimeSeconds = (SystemClock.elapsedRealtime() - startTime) / 1000

        GlobalScope.launch {
            try {
                scoreDao.addScore(Score(0, Date(), playerName, moves, elapsedTimeSeconds))
                Log.d("ScoreAdded", "Score added successfully.")
            } catch (e: Exception) {
                Log.e("ScoreAddError", "Error adding score to database", e)
            }
        }

        showGameResultDialog()
    }

    /**
     * Gets the resource ID for a given card position.
     */
    private fun getResourceId(position: Int): Int {
        return resources.getIdentifier("image${position + 1}", "id", packageName)
    }

    /**
     * Gets the image resource ID for a card at a given position.
     */
    private fun getImageResourceId(position: Int): Int {
        val card = gameViewModel.getCards()[position]
        return resources.getIdentifier(card.imagePath, "drawable", packageName)
    }

    /**
     * Starts the chronometer when the game begins.
     */
    private fun startChronometer() {
        startTime = SystemClock.elapsedRealtime()
        chronometer.base = startTime
        chronometer.start()
    }

    /**
     * Stops the chronometer when the game ends.
     */
    private fun stopChronometer() {
        chronometer.stop()
    }

    /**
     * Displays a dialog with the game result, including the player's name, moves, and time.
     */
    private fun showGameResultDialog() {
        if (dialogClosed) {
            return
        }

        val playerName = playerName.text.toString()
        val moves = gameViewModel.getNumberOfMoves()

        val seconds = (SystemClock.elapsedRealtime() - startTime) / 1000

        val dialogMessage = getString(R.string.game_result_message, playerName, moves, seconds)

        val alertDialog = AlertDialog.Builder(this)
            .setTitle(R.string.game_result_title)
            .setMessage(dialogMessage)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                navigateToMainActivity()
                dialogClosed = true
            }
            .setCancelable(false)
            .create()

        alertDialog.show()
    }

    /**
     * Navigates back to the main activity after the game is complete.
     */
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Resumes the activity and navigates to the main activity if the game dialog is closed.
     */
    override fun onResume() {
        super.onResume()

        if (dialogClosed) {
            navigateToMainActivity()
        }
    }
}