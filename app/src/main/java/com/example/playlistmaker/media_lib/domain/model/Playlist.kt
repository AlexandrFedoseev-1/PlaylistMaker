package com.example.playlistmaker.media_lib.domain.model


data class Playlist(
    val playlistId: Int,
    val name: String,
    val description: String,
    val imageUri: String,
    val tracksId: List<String>,
    val tracksCount: Int
)
