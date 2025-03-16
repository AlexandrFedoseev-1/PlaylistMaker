package com.example.playlistmaker.db

import com.example.playlistmaker.db.data.FavoriteTracksEntity
import com.example.playlistmaker.search.domain.models.Track

class TrackEntityMapper {
    fun map(track: Track): FavoriteTracksEntity {
        return FavoriteTracksEntity(
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

    fun map(track: FavoriteTracksEntity): Track {
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