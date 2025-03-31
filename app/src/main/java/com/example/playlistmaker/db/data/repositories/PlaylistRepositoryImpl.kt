package com.example.playlistmaker.db.data.repositories

import android.util.Log
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
        try {
            appDatabase.playlistDao().deletePlaylist(playlistEntityMapper.map(playlist))
        } catch (e: Exception) {
            Log.e("PlaylistRepo", "Error deleting playlist", e)
        }
        playlist.tracksId.forEach {
            checkTrack(it)
        }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(playlistEntityMapper.map(playlist))
    }

    override fun getPlaylist(): Flow<List<Playlist>> =
        appDatabase.playlistDao().getPlaylist().map { playlistEntity ->
            playlistEntity.map { playlistEntityMapper.map(it) }
        }

    override fun getTracksFromPlaylist(playlistTracksId: List<String>): Flow<List<Track>> {
        return appDatabase.addTracksToPlaylistsDao().getTracksById(playlistTracksId)
            .map { tracks -> tracks.map { addTracksEntityMapper.map(it) } }
    }

//    override suspend fun getPlaylistTracksId(): List<String> {
//        return appDatabase.playlistDao().getPlaylistTracksId()
//    }

    override fun getPlaylistById(playlistId: Int): Flow<Playlist>{
        return appDatabase.playlistDao().getPlaylistById(playlistId).map { playlistEntityMapper.map(it) }
    }

    override suspend fun deleteTrackFromPlaylist(trackId: String, playlist: Playlist) {
        val updatedTracksId = playlist.tracksId.toMutableList().apply {
            remove(trackId)
        }
        val updatedPlaylist = playlist.copy(
            tracksId = updatedTracksId,
            tracksCount = updatedTracksId.size
        )
        appDatabase.playlistDao().updatePlaylist(playlistEntityMapper.map(updatedPlaylist))
        checkTrack(trackId)
    }
    private suspend fun checkTrack(trackId: String){
        val allTracksId = appDatabase.playlistDao().getAllPlaylist().flatMap { it.tracksId }
        if (!allTracksId.contains(trackId)){
            appDatabase.addTracksToPlaylistsDao().deleteTrackById(trackId)
        }
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist): Boolean {
        try {
            val updatedTracksId = playlist.tracksId.toMutableList().apply {
                add(track.trackId)
            }
            val updatedPlaylist = playlist.copy(
                tracksId = updatedTracksId,
                tracksCount = updatedTracksId.size
            )
            updatePlaylist(updatedPlaylist)
            appDatabase.addTracksToPlaylistsDao().insertTrack(addTracksEntityMapper.map(track))
            return true
        } catch (e: Exception) {
            return true
        }

    }
}