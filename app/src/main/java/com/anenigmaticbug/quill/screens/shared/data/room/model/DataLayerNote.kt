package com.anenigmaticbug.quill.screens.shared.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anenigmaticbug.quill.screens.shared.core.model.Id
import org.threeten.bp.LocalDateTime

@Entity(tableName = "Notes")
data class DataLayerNote(
    @PrimaryKey(autoGenerate = true) val id: Id,
    val heading: String,
    val content: String,
    val tags: List<String>,
    val lastModified: LocalDateTime,
    val isTrashed: Boolean
)