package com.anenigmaticbug.quill.di.shared

import dagger.Component
import javax.inject.Singleton

@Singleton @Component(modules = [AppModule::class])
interface AppComponent