package com.example.pexeso.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pexeso.database.Score
import com.example.pexeso.databinding.ScoreRowBinding
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * ScoreAdapter is responsible for binding the score data to the RecyclerView.
 * It displays a list of scores, with player name, date, score, and time formatted in a readable way.
 */

class ScoreAdapter() : ListAdapter<Score, ScoreAdapter.ScoreViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Score>() {
            /**
             * Compares if two items are the same based on their unique identifier (date in this case).
             */
            override fun areItemsTheSame(oldItem: Score, newItem: Score): Boolean {
                return oldItem.date == newItem.date
            }

            /**
             * Checks if the contents of two items are the same.
             */
            override fun areContentsTheSame(oldItem: Score, newItem: Score): Boolean {
                return oldItem == newItem
            }
        }
    }

    /**
     * Called when the RecyclerView needs a new ViewHolder to represent an item.
     * Inflates the layout for each individual item and creates a ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val viewHolder = ScoreViewHolder(
            ScoreRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        return viewHolder
    }

    /**
     * Binds the score data to the views in the ViewHolder at the specified position.
     */
    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * ViewHolder class that holds the views for each item in the RecyclerView.
     * This class is responsible for binding the Score object to the views.
     */
    class ScoreViewHolder(
        private var binding: ScoreRowBinding
    ): RecyclerView.ViewHolder(binding.root) {
        private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        fun bind(score: Score) {
            binding.name.text = score.name
            binding.date.text = dateFormat.format(score.date)
            binding.score.text = score.score.toString()
            val minutes = score.time / 60
            val seconds = score.time % 60
            val timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
            binding.time.text = timeFormatted
        }
    }
}