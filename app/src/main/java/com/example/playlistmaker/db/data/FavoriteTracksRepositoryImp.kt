package com.example.playlistmaker.db.data

import com.example.playlistmaker.db.TrackDbConvertor
import com.example.playlistmaker.db.domain.api.FavoriteTracksRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class FavoriteTracksRepositoryImp(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) : FavoriteTracksRepository {
    override suspend fun addToFavorite(track: Track) {
        appDatabase.favoriteTrackDao().insertTrack(trackDbConvertor.map(track))
    }

    override suspend fun deleteFromFavorite(track: Track) {
        appDatabase.favoriteTrackDao().deleteTrack(trackDbConvertor.map(track))
    }

    override fun getTracks(): Flow<List<Track>> =
        appDatabase.favoriteTrackDao().getTracks()
            .map { tracksEntity ->
                tracksEntity.map { trackDbConvertor.map(it) }.map { track ->
                    track.copy(isFavorite = true)
                }.reversed()
            }


}