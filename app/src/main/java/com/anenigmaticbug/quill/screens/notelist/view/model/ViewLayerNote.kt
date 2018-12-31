package com.anenigmaticbug.quill.screens.notelist.view.model

import com.anenigmaticbug.quill.screens.shared.core.model.Id

data class ViewLayerNote(
    val id: Id,
    val heading: String,
    val content: String,
    val lastModified: String,
    val isTrashed: Boolean = false
)