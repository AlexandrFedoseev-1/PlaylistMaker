package com.example.playlistmaker.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.playlistmaker.db.data.ListStingConverter

@Entity(tableName = "playlist")
@TypeConverters(ListStingConverter::class)
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int,
    val name: String,
    val description: String,
    val imageUri: String,
    val tracksId: List<String> = emptyList(),
    val tracksCount: Int = 0
)
