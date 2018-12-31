package com.anenigmaticbug.quill.screens.shared.data.repo

import com.anenigmaticbug.quill.screens.shared.core.model.Id
import com.anenigmaticbug.quill.screens.shared.core.model.Note
import io.reactivex.Flowable

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
     * Gives all the tags used in all the notes(trashed or un-trashed).
     * */
    fun getAllTags(): Flowable<List<String>>
}