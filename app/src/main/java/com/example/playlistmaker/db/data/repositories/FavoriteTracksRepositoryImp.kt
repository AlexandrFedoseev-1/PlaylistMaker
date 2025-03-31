package com.example.playlistmaker.db.data.repositories

import com.example.playlistmaker.db.mappers.TrackEntityMapper
import com.example.playlistmaker.db.data.AppDatabase
import com.example.playlistmaker.db.domain.api.FavoriteTracksRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class FavoriteTracksRepositoryImp(
    private val appDatabase: AppDatabase,
    private val trackEntityMapper: TrackEntityMapper
) : FavoriteTracksRepository {
    override suspend fun addToFavorite(track: Track) {
        appDatabase.favoriteTrackDao().insertTrack(trackEntityMapper.map(track))
    }

    override suspend fun deleteFromFavorite(track: Track) {
        appDatabase.favoriteTrackDao().deleteTrack(trackEntityMapper.map(track))
    }

    override fun getTracks(): Flow<List<Track>> =
        appDatabase.favoriteTrackDao().getTracks()
            .map { tracksEntity ->
                tracksEntity.map { trackEntityMapper.map(it) }.map { track ->
                    track.copy(isFavorite = true)
                }.reversed()
            }


    override suspend fun getTracksId(): List<String> =
        appDatabase.favoriteTrackDao().getTracksId()

}