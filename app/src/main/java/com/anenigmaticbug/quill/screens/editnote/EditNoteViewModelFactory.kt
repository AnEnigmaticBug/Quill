package com.anenigmaticbug.quill.screens.editnote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anenigmaticbug.quill.QuillApp
import com.anenigmaticbug.quill.di.editnote.EditNoteModule
import com.anenigmaticbug.quill.screens.shared.core.model.Id
import com.anenigmaticbug.quill.screens.shared.data.repo.NoteRepository
import javax.inject.Inject

class EditNoteViewModelFactory(private val id: Id?) : ViewModelProvider.Factory {

    @Inject
    lateinit var noteRepository: NoteRepository

    init {
        QuillApp.appComponent.newEditNoteComponent(EditNoteModule()).inject(this)
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditNoteViewModel(noteRepository, id) as T
    }
}