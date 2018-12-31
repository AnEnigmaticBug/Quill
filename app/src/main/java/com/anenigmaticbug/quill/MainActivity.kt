package com.anenigmaticbug.quill

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anenigmaticbug.quill.screens.notelist.view.NoteListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_main)

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.navHostPOV, NoteListFragment())
                .commit()
        }
    }
}
