package com.anenigmaticbug.quill.screens.shared.data.room

import androidx.room.TypeConverter
import org.threeten.bp.LocalDateTime

class SharedTypeConverters {

    @TypeConverter
    fun fromStringToLocalDateTime(st: String): LocalDateTime = LocalDateTime.parse(st)

    @TypeConverter
    fun fromLocalDateTimeToString(dt: LocalDateTime): String = dt.toString()

    @TypeConverter
    fun fromStringListToString(ls: List<String>): String = ls.joinToString("~")

    @TypeConverter
    fun fromStringToStringList(st: String): List<String> = st.split("~")
}