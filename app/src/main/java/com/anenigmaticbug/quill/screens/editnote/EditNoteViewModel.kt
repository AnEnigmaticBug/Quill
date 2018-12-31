package com.anenigmaticbug.quill.screens.editnote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anenigmaticbug.quill.screens.editnote.view.model.UiOrder
import com.anenigmaticbug.quill.screens.editnote.view.model.ViewLayerNote
import com.anenigmaticbug.quill.screens.shared.core.model.Id
import com.anenigmaticbug.quill.screens.shared.core.model.Note
import com.anenigmaticbug.quill.screens.shared.data.repo.NoteRepository
import com.anenigmaticbug.quill.util.set
import com.anenigmaticbug.quill.util.toMut
import io.reactivex.disposables.CompositeDisposable
import org.threeten.bp.LocalDateTime

/**
 * @param id  can be null if and only if a new note is being created
 * */
class EditNoteViewModel(private val nRepo: NoteRepository, private val id: Id?) : ViewModel() {

    val order: LiveData<UiOrder> = MutableLiveData()
    val toast: LiveData<String?> = MutableLiveData()

    val isValidTag: LiveData<Boolean> = MutableLiveData()


    // The source of truth for the edit note screen.
    private lateinit var note: Note
    private lateinit var unmodifiedNote: Note


    private val d1 = CompositeDisposable()


    init {
        // This starts out as false because blank strings aren't allowed as tags.
        isValidTag.toMut().postValue(false)

        if(id == null) {
            unmodifiedNote = Note(0L)
            note = unmodifiedNote
            order.toMut().postValue(UiOrder.ShowWorking(note.toViewLayer()))
        } else {
            d1.set(nRepo.getNoteById(id)
                .take(1)
                .subscribe(
                    { _note ->
                        unmodifiedNote = _note
                        note = unmodifiedNote
                        order.toMut().postValue(UiOrder.ShowWorking(_note.toViewLayer()))
                    },
                    {
                        order.toMut().postValue(UiOrder.ShowFailure("Couldn't display your note :("))
                    }
                ))
        }
    }


    fun onUpdateHeadingAction(heading: String) {
        note = note.copy(heading = heading)
        order.toMut().postValue(UiOrder.ShowWorking(note.toViewLayer()))
    }

    fun onUpdateContentAction(content: String) {
        note = note.copy(content = content)
        order.toMut().postValue(UiOrder.ShowWorking(note.toViewLayer()))
    }

    fun onTagToBeInsertedTextChanged(tag: String) {
        isValidTag.toMut().postValue(tag.isNotBlank() && tag !in note.tags)
    }

    fun onInsertTagAction(tag: String) {
        if(tag.isNotBlank() && tag !in note.tags) {
            note = note.copy(tags = note.tags.toMutableList().also { it.add(tag) })
            order.toMut().postValue(UiOrder.ShowWorking(note.toViewLayer()))
            isValidTag.toMut().postValue(false)
        }
    }

    fun onDeleteTagAction(tag: String) {
        note = note.copy(tags = note.tags.toMutableList().also { it.remove(tag) })
        order.toMut().postValue(UiOrder.ShowWorking(note.toViewLayer()))
    }


    private fun saveNote() {
        // Don't save the note if it hasn't been modified.
        if(note == unmodifiedNote) {
            return
        }

        // Don't allow saving note with nothing within it.
        if(note.heading.isBlank() && note.content.isBlank()) {
            toast.toMut().postValue("Can't save a blank note")
            return
        }

        // Blank headings are replaced by 'Untitled' and the last modified time is set to the time when saving occurs.
        note = note.copy(
            heading = if(note.heading.isBlank()) { "Untitled" } else { note.heading }, lastModified = LocalDateTime.now()
        )

        // The actual inserting/updating part.
        when(id) {
            null -> nRepo.insertNote(note).subscribe(
                {

                },
                {
                    toast.toMut().postValue("Error while saving the note :(")
                }
            )
            else -> nRepo.updateNote(note).subscribe(
                {

                },
                {
                    toast.toMut().postValue("Error while saving the note :(")
                }
            )
        }
    }


    private fun Note.toViewLayer(): ViewLayerNote {
        return ViewLayerNote(id, heading, content, tags.filter { it != "" }, lastModified, isTrashed)
    }


    override fun onCleared() {
        super.onCleared()
        saveNote()
        d1.clear()
    }
}