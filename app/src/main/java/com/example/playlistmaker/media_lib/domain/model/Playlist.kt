package com.example.playlistmaker.media_lib.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val playlistId: Int,
    val name: String,
    val description: String,
    val imageUri: String,
    val tracksId: List<String>,
    val tracksCount: Int
) : Parcelable
