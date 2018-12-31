package com.anenigmaticbug.quill.screens.shared.data.room

import androidx.room.*
import com.anenigmaticbug.quill.screens.shared.core.model.Id
import com.anenigmaticbug.quill.screens.shared.data.room.model.DataLayerNote
import io.reactivex.Flowable

@Dao
interface NoteDao {

    @Query("SELECT * FROM Notes")
    fun getAllNotes(): Flowable<List<DataLayerNote>>

    @Query("SELECT * FROM Notes WHERE id = :id")
    fun getNoteById(id: Id): Flowable<DataLayerNote>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertNote(note: DataLayerNote)

    @Update
    fun updateNote(note: DataLayerNote)

    @Query("DELETE FROM Notes WHERE id = :id")
    fun deleteNoteById(id: Id)

    @Query("DELETE FROM Notes")
    fun deleteAllNotes()
}