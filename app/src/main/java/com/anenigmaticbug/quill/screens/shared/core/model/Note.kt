package com.anenigmaticbug.quill.screens.shared.core.model

import org.threeten.bp.LocalDateTime

typealias Id = Long

data class Note(
    val id: Id = 0,
    val heading: String = "",
    val content: String = "",
    val tags: List<String> = listOf(),
    val lastModified: LocalDateTime = LocalDateTime.now(),
    val isTrashed: Boolean = false
)