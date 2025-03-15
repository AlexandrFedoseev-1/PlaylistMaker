package com.example.playlistmaker.db.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.db.data.dao.FavoriteTracksDao

@Database (version = 1, entities = [FavoriteTracksEntity::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun favoriteTrackDao(): FavoriteTracksDao
}