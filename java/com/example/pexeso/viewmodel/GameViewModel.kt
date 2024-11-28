package com.example.pexeso.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.pexeso.model.Card
import kotlin.random.Random

/**
 * ViewModel class for the Pexeso game. It manages the game's data, including card states and moves.
 * It interacts with the UI through the GameListener interface, notifying the UI of card flips,
 * matches, mismatches, and when the game is complete.
 */
class GameViewModel(private val listener: GameListener): ViewModel() {

    private var flippedCards: MutableList<Card> = mutableListOf()
    private var numberOfMoves = 0
    private lateinit var cards: List<Card>

    // Initialization block that is called when the ViewModel is created
    init {
        initializeGame()
    }

    /**
     * Initializes the game by creating and shuffling cards.
     */
    fun initializeGame() {
        cards = createCardList()
        cards = shuffledCards()
        numberOfMoves = 0
    }

    /**
     * Creates a list of cards based on predefined image paths.
     * The list consists of pairs of cards, each with the same image.
     */
    fun createCardList(): List<Card> {
        val cardImages = listOf("apple", "avocado", "banana", "blueberry", "cherry", "grapes", "pineapple", "orange", "strawberry", "watermelon")
        val cardsList = mutableListOf<Card>()

        // Create pairs of cards, each with the same image path
        for (id in 1..(cardImages.size * 2)) {
            val imageIndex = (id - 1) % cardImages.size
            val imagePath = cardImages[imageIndex]
            val card = Card(id, imagePath)
            cardsList.add(card)
        }

        return cardsList
    }

    /**
     * Shuffles the list of cards to randomize their order.
     */
    private fun shuffledCards(): List<Card> {
        return cards.shuffled(Random(System.currentTimeMillis()))
    }

    /**
     * Handles the logic when a card is clicked by the player.
     * It flips the card and checks if a match has been made.
     */
    fun onCardClicked(position: Int) {
            val card = cards[position]

            if (!card.isFlipped && flippedCards.size < 2) {
                flipCard(position)
                flippedCards.add(card)

                if (flippedCards.size == 2) {
                    incrementMoves()
                    checkForMatch()
                }
            }
    }

    /**
     * Flips the card at the given position and notifies the listener.
     */
    private fun flipCard(position: Int) {
        cards[position].isFlipped = true
        listener.onCardFlipped(position)
    }

    /**
     * Checks if the two flipped cards match.
     * If they match, mark them as matched and notify the listener.
     * If they don't match, flip them back and notify the listener.
     */
    private fun checkForMatch() {

        if (flippedCards[0].imagePath == flippedCards[1].imagePath) {
            flippedCards.forEach { it.isMatched = true }
            listener.onMatchFound()
        } else {
            listener.onMismatch()
            flippedCards.forEach {it.isFlipped = false }
        }

        flippedCards.clear()

        if (isGameComplete()) {
            listener.onGameComplete()
        }
    }

    /**
     * Checks if all the cards in the game have been matched.
     */
    private fun isGameComplete(): Boolean {
        return cards.all { it.isMatched }
    }

    /**
     * Returns a list of the cards that have been flipped but not yet matched.
     */
    fun getFlippedCards(): List<Card> {
        return flippedCards.toList()
    }

    /**
     * Returns the full list of cards in the game.
     */
    fun getCards(): List<Card> {
        return cards.toList()
    }

    /**
     * Returns the number of moves made by the player.
     */
    fun getNumberOfMoves(): Int {
        return numberOfMoves
    }

    /**
     * Increments the move count and logs the current number of moves.
     */
    private fun incrementMoves() {
        numberOfMoves++
        Log.d("GameViewModel", "Number of moves: $numberOfMoves")
    }

    /**
     * Listener interface to notify the UI of game events such as card flips, matches, and game completion.
     */
    interface GameListener {
        fun onCardFlipped(position: Int)
        fun onMatchFound()
        fun onMismatch()
        fun onGameComplete()
    }
}