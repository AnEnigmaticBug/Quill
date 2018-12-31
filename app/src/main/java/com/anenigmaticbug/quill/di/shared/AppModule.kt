package com.anenigmaticbug.quill.di.shared

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides @Singleton
    fun providesApplication(): Application {
        return application
    }
}