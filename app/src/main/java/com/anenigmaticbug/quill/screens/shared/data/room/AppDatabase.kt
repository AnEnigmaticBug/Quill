package com.anenigmaticbug.quill.screens.shared.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.anenigmaticbug.quill.screens.shared.data.room.model.DataLayerNote

@Database(entities = [DataLayerNote::class], version = 1, exportSchema = true)
@TypeConverters(SharedTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
}