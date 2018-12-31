package com.anenigmaticbug.quill.screens.shared.data.repo

import com.anenigmaticbug.quill.screens.shared.core.model.Id
import com.anenigmaticbug.quill.screens.shared.core.model.Note
import com.anenigmaticbug.quill.screens.shared.data.room.NoteDao
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class NoteRepositoryImpl(private val noteDao: NoteDao) : NoteRepository {

    override fun getAllNotes(): Flowable<List<Note>> {
        return noteDao.getAllNotes()
            .subscribeOn(Schedulers.io())
            .map { it.map { Note(it.id , it.heading, it.content, it.tags, it.lastModified, it.isTrashed) } }
    }

    override fun getNoteById(id: Id): Flowable<Note> {
        return noteDao.getNoteById(id)
            .subscribeOn(Schedulers.io())
            .map { Note(it.id, it.heading, it.content, it.tags, it.lastModified, it.isTrashed) }
    }

    override fun getAllTags(): Flowable<List<String>> {
        return noteDao.getAllNotes()
            .subscribeOn(Schedulers.io())
            .map { it.map { it.tags }.flatten().distinct().filter { it != "" } }
    }
}