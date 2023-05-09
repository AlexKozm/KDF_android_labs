package com.example.roomlab

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

@WorkerThread
class WordRepository(
    private val wordDao: WordDao
) {
    val allWords: Flow<List<Word>> = wordDao.getAlphabetizedWords()
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }

    suspend fun deleteAll() {
        wordDao.deleteAll()
    }

    suspend fun deleteWord(word: Word) {
        wordDao.deleteWord(word)
    }

}