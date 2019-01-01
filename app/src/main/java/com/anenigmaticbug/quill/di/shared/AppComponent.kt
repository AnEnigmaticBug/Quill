package com.anenigmaticbug.quill.di.shared

import com.anenigmaticbug.quill.di.editnote.EditNoteComponent
import com.anenigmaticbug.quill.di.editnote.EditNoteModule
import com.anenigmaticbug.quill.di.notelist.NoteListComponent
import com.anenigmaticbug.quill.di.notelist.NoteListModule
import dagger.Component
import javax.inject.Singleton

@Singleton @Component(modules = [AppModule::class])
interface AppComponent {

    fun newNoteListComponent(m1: NoteListModule): NoteListComponent

    fun newEditNoteComponent(m1: EditNoteModule): EditNoteComponent
}