package com.anenigmaticbug.quill.di.shared

import android.app.Application
import androidx.room.Room
import com.anenigmaticbug.quill.screens.shared.data.repo.NoteRepository
import com.anenigmaticbug.quill.screens.shared.data.repo.NoteRepositoryImpl
import com.anenigmaticbug.quill.screens.shared.data.room.AppDatabase
import com.anenigmaticbug.quill.screens.shared.data.room.NoteDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides @Singleton
    fun providesNoteRepository(noteDao: NoteDao): NoteRepository {
        return NoteRepositoryImpl(noteDao)
    }

    @Provides @Singleton
    fun providesNoteDao(appDatabase: AppDatabase): NoteDao {
        return appDatabase.noteDao()
    }

    @Provides @Singleton
    fun providesAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "quill.db")
            .build()
    }

    @Provides @Singleton
    fun providesApplication(): Application {
        return application
    }
}