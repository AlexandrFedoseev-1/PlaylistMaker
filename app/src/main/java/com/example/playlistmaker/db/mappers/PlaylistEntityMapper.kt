package com.example.playlistmaker.db.mappers

import com.example.playlistmaker.db.data.entity.PlaylistEntity
import com.example.playlistmaker.media_lib.domain.model.Playlist


class PlaylistEntityMapper {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlistId =  playlist.playlistId,
            name = playlist.name,
            description = playlist.description,
            imageUri = playlist.imageUri,
            tracksId = playlist.tracksId,
            tracksCount = playlist.tracksCount
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlistId =  playlist.playlistId,
            name = playlist.name,
            description = playlist.description,
            imageUri = playlist.imageUri,
            tracksId = playlist.tracksId,
            tracksCount = playlist.tracksCount
        )
    }
}