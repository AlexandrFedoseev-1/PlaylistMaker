package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.App
import com.example.playlistmaker.db.TrackDbConvertor
import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.api.AudioPlayer
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.SearchHistoryLocalDataSource
import com.example.playlistmaker.db.data.AppDatabase
import com.example.playlistmaker.db.data.FavoriteTracksRepositoryImp
import com.example.playlistmaker.db.domain.api.FavoriteTracksRepository
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.network.TrackSearchApi
import com.example.playlistmaker.search.data.network.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.settings.data.SettingsLocalDataSource
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.api.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

//    NetworkClient

    single<TrackSearchApi> {
        Retrofit.Builder().baseUrl(RetrofitNetworkClient.TRACK_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(TrackSearchApi::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(trackService = get())
    }

//  LocalDataSource

    single(named("SettingsSharedPref")) {
        androidContext().getSharedPreferences(App.SETTING_PREFERENCES, Context.MODE_PRIVATE)
    }

    single(named("SearchHistorySharedPref")) {
        androidContext().getSharedPreferences(
            SearchHistoryLocalDataSource.SEARCH_HISTORY_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    single {
        SettingsLocalDataSource(
            context = androidContext(),
            sharedPref = get(named("SettingsSharedPref"))
        )
    }
    single {
        SearchHistoryLocalDataSource(sharedPref = get(named("SearchHistorySharedPref")), appDatabase = get())
    }

//    Repositories
    factory {
        MediaPlayer()
    }

    factory<AudioPlayer> {
        MediaPlayerRepositoryImpl(mediaPlayer = get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(networkClient = get(), searchHistoryLocalDataSource = get(), appDatabase = get())
    }
    single<SettingsRepository> {
        SettingsRepositoryImpl(settingsLocalDataSource = get())
    }

//    DataBase

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").build()
    }

    factory { TrackDbConvertor() }

    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImp(get(), get())
    }


}