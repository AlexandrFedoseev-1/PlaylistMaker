package com.example.playlistmaker.db.data

import androidx.room.TypeConverter

object ListStingConverter {
    @TypeConverter
    fun toListString(list: List<String>): String {
        return list.joinToString(separator = ",")
    }

    @TypeConverter
    fun toListString(data: String): List<String> {
        if (data.isEmpty()) return emptyList()
        return data.split(",")
    }
}