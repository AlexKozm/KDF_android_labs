package com.example.roomlab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class WordListAdapter : ListAdapter<Word, WordListAdapter.WordViewHolder>(WordsComparator()) {

//    val mWords: List<Word> = emptyList()

    class WordViewHolder(itemView: View) : ViewHolder(itemView) {
        private val wordItemView: TextView = itemView.findViewById(R.id.textView)
        fun bind(text: String?) {
            wordItemView.text = text
        }
        companion object {
            fun create(parent: ViewGroup): WordViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return WordViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
        = WordViewHolder.create(parent)

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.word)
    }

    fun getWordAtPosition(position: Int): Word = getItem(position)

    class WordsComparator : DiffUtil.ItemCallback<Word>() {
        override fun areItemsTheSame(oldItem: Word, newItem: Word) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Word, newItem: Word) =
            oldItem.word == newItem.word

    }
}