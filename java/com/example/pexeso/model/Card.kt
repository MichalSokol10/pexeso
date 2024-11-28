package com.example.pexeso.model

/**
 * Represents a card in the Pexeso game.
 *
 * @param id The unique identifier for the card.
 * @param imagePath The path to the image that will be displayed on the card.
 * @param isFlipped Indicates whether the card is currently flipped.
 * @param isMatched Indicates whether the card has been matched with another card.
 */

data class Card(val id: Int, val imagePath: String, var isFlipped: Boolean = false, var isMatched: Boolean = false)