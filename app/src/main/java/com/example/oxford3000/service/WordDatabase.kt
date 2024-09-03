package com.example.oxford3000.service

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.oxford3000.model.WordsSelected

@Database(entities = [WordsSelected::class], version = 1)
abstract class WordDatabase:RoomDatabase() {
    abstract fun wordDao():WordsSelectedDao

}