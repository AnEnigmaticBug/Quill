package com.anenigmaticbug.quill.screens.shared.data.repo

import com.anenigmaticbug.quill.screens.shared.core.model.Id
import com.anenigmaticbug.quill.screens.shared.core.model.Note
import com.anenigmaticbug.quill.screens.shared.data.room.NoteDao
import com.anenigmaticbug.quill.screens.shared.data.room.model.DataLayerNote
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
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

    override fun insertNote(note: Note): Single<Id> {
        return Single.fromCallable {
            noteDao.insertNote(
                if (note.id == 0L) { note } else { note.copy(id = 0) }.toDataLayerNote()
            )
        }.subscribeOn(Schedulers.io())
    }

    override fun updateNote(note: Note): Completable {
        return Completable.fromAction {
            noteDao.updateNote(note.toDataLayerNote())
        }.subscribeOn(Schedulers.io())
    }

    override fun deleteNoteById(id: Id): Completable {
        return Completable.fromAction {
            noteDao.deleteNoteById(id)
        }.subscribeOn(Schedulers.io())
    }

    private fun Note.toDataLayerNote(): DataLayerNote {
        return DataLayerNote(id, heading, content, tags, lastModified, isTrashed)
    }
}