package com.example.playlistmaker.db.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.playlistmaker.db.data.dao.AddTracksToPlaylistsDao
import com.example.playlistmaker.db.data.dao.FavoriteTracksDao
import com.example.playlistmaker.db.data.dao.PlaylistDao
import com.example.playlistmaker.db.data.entity.AddTracksToPlaylistsEntity
import com.example.playlistmaker.db.data.entity.FavoriteTracksEntity
import com.example.playlistmaker.db.data.entity.PlaylistEntity

@Database (version = 6, entities = [FavoriteTracksEntity::class, PlaylistEntity::class, AddTracksToPlaylistsEntity::class])
@TypeConverters(ListStingConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun favoriteTrackDao(): FavoriteTracksDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun addTracksToPlaylistsDao(): AddTracksToPlaylistsDao
}