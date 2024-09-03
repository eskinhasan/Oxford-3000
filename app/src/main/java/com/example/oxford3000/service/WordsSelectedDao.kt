package com.example.oxford3000.service

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.oxford3000.model.WordsSelected


@Dao
interface WordsSelectedDao {
    @Query("SELECT * FROM wordsselected")
    suspend fun getAll():List<WordsSelected>

    @Insert
    suspend  fun insert(vararg wordsSelected: WordsSelected)

    @Delete
    suspend fun delete(wordsSelected: WordsSelected)





}