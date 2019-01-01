package com.anenigmaticbug.quill.di.editnote

import com.anenigmaticbug.quill.screens.editnote.EditNoteViewModelFactory
import dagger.Subcomponent

@Subcomponent(modules = [EditNoteModule::class])
interface EditNoteComponent {

    fun inject(factory: EditNoteViewModelFactory)
}