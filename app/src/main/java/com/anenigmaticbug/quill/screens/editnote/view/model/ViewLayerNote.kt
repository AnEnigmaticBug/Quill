package com.anenigmaticbug.quill.screens.editnote.view.model

import com.anenigmaticbug.quill.screens.shared.core.model.Id
import org.threeten.bp.LocalDateTime

data class ViewLayerNote(
    val id: Id,
    val heading: String,
    val content: String,
    val tags: List<String>,
    val lastModified: LocalDateTime,
    val isTrashed: Boolean
) {

    val wordCount: Int
        get() {
            return when(content) {
                ""   -> 0
                else -> content.count { it == ' ' }.inc()
            }
        }

    val charCount: Int
        get() {
            return content.length
        }
}