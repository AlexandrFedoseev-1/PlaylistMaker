package com.example.playlistmaker.db.data.repositories

import com.example.playlistmaker.db.data.AppDatabase
import com.example.playlistmaker.db.domain.api.PlaylistRepository
import com.example.playlistmaker.db.mappers.AddTracksEntityMapper
import com.example.playlistmaker.db.mappers.PlaylistEntityMapper
import com.example.playlistmaker.media_lib.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistEntityMapper: PlaylistEntityMapper,
    private val addTracksEntityMapper: AddTracksEntityMapper
) : PlaylistRepository {

    override suspend fun insertPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(playlistEntityMapper.map(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().deletePlaylist(playlistEntityMapper.map(playlist))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(playlistEntityMapper.map(playlist))
    }

    override fun getPlaylist(): Flow<List<Playlist>> =
         appDatabase.playlistDao().getPlaylist().map { playlistEntity ->
             playlistEntity.map { playlistEntityMapper.map(it) }
         }


    override suspend fun getPlaylistTracksId(): List<String> {
        return appDatabase.playlistDao().getPlaylistTracksId()
    }
    override suspend fun addTrackToPlaylist(track:Track, playlist: Playlist) : String {
        try {
            val updatedTracksId = playlist.tracksId.toMutableList().apply {
                add(track.trackId)
            }
            val updatedPlaylist = playlist.copy(
                tracksId = updatedTracksId,
                tracksCount = playlist.tracksCount + 1
            )
            updatePlaylist(updatedPlaylist)
            appDatabase.addTracksToPlaylistsDao().insertTrack(addTracksEntityMapper.map(track))
            return "Добавлено в плейлист '${updatedPlaylist.name}'"
        }catch (e:Exception){
            return "Ошибка добавления("
        }

    }
}