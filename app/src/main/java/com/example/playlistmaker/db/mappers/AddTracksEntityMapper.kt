package com.example.playlistmaker.db.mappers

import com.example.playlistmaker.db.data.entity.AddTracksToPlaylistsEntity
import com.example.playlistmaker.search.domain.models.Track

class AddTracksEntityMapper {
    fun map(track: Track): AddTracksToPlaylistsEntity {
        return AddTracksToPlaylistsEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun map(track: AddTracksToPlaylistsEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }
}