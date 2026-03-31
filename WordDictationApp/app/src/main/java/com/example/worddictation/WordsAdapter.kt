package com.example.worddictation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class WordsAdapter(
    private var words: List<String>,
    private val onWordClick: (String, Int) -> Unit
) : RecyclerView.Adapter<WordsAdapter.WordViewHolder>() {
    
    private var currentPosition = -1
    
    class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordTextView: TextView = itemView.findViewById(R.id.wordTextView)
        val indexTextView: TextView = itemView.findViewById(R.id.indexTextView)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_word, parent, false)
        return WordViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = words[position]
        
        holder.wordTextView.text = word
        holder.indexTextView.text = "${position + 1}."
        
        // 设置当前朗读词语的高亮
        if (position == currentPosition) {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.current_word_highlight)
            )
            holder.wordTextView.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.primary)
            )
            holder.wordTextView.textSize = 20f
        } else {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, android.R.color.transparent)
            )
            holder.wordTextView.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.word_normal)
            )
            holder.wordTextView.textSize = 18f
        }
        
        // 点击事件
        holder.itemView.setOnClickListener {
            onWordClick(word, position)
        }
    }
    
    override fun getItemCount(): Int = words.size
    
    fun updateWords(newWords: List<String>) {
        words = newWords
        notifyDataSetChanged()
    }
    
    fun setCurrentPosition(position: Int) {
        val oldPosition = currentPosition
        currentPosition = position
        if (oldPosition >= 0) {
            notifyItemChanged(oldPosition)
        }
        if (position >= 0) {
            notifyItemChanged(position)
        }
    }
    
    fun getWordAt(position: Int): String {
        return if (position in 0 until words.size) {
            words[position]
        } else {
            ""
        }
    }
    
    fun clearSelection() {
        val oldPosition = currentPosition
        currentPosition = -1
        if (oldPosition >= 0) {
            notifyItemChanged(oldPosition)
        }
    }
}