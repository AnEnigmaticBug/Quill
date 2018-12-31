package com.anenigmaticbug.quill.screens.shared.data.repo

import com.anenigmaticbug.quill.screens.shared.core.model.Id
import com.anenigmaticbug.quill.screens.shared.core.model.Note
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * This repository facilitates the access to the notes data.
 * */
interface NoteRepository {

    /**
     * Gives all notes(trashed or un-trashed).
     * */
    fun getAllNotes(): Flowable<List<Note>>

    /**
     * Gives the note having the specified id.
     * */
    fun getNoteById(id: Id): Flowable<Note>

    /**
     * Inserts the note ignoring it's id and gives the inserted note's autogenerated id.
     * */
    fun insertNote(note: Note): Single<Id>

    /**
     * Updates the note having the same id as the  passed in note's id.
     * */
    fun updateNote(note: Note): Completable

    /**
     * Deletes the note having the same id as the  passed in note's id.
     * */
    fun deleteNoteById(id: Id): Completable

    /**
     * Gives all the tags used in all the notes(trashed or un-trashed).
     * */
    fun getAllTags(): Flowable<List<String>>
}