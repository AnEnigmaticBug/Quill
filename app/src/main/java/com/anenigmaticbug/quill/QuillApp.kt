package com.anenigmaticbug.quill

import android.app.Application
import com.anenigmaticbug.quill.di.shared.AppComponent
import com.anenigmaticbug.quill.di.shared.AppModule
import com.anenigmaticbug.quill.di.shared.DaggerAppComponent
import com.jakewharton.threetenabp.AndroidThreeTen

class QuillApp : Application() {

    companion object {

        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

        AndroidThreeTen.init(this)
    }
}