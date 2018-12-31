package com.anenigmaticbug.quill.screens.notelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anenigmaticbug.quill.QuillApp
import com.anenigmaticbug.quill.di.notelist.NoteListModule
import com.anenigmaticbug.quill.screens.shared.data.repo.NoteRepository
import javax.inject.Inject

class NoteListViewModelFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var noteRepository: NoteRepository

    init {
        QuillApp.appComponent.newNoteListComponent(NoteListModule()).inject(this)
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoteListViewModel(noteRepository) as T
    }
}