package com.anenigmaticbug.quill.di.notelist

import com.anenigmaticbug.quill.screens.notelist.NoteListViewModelFactory
import dagger.Subcomponent

@Subcomponent(modules = [NoteListModule::class])
interface NoteListComponent {

    fun inject(factory: NoteListViewModelFactory)
}