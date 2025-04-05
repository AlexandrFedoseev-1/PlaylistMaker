package com.example.playlistmaker.media_lib.ui.add_playlist

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.db.domain.api.PlaylistInteractor
import com.example.playlistmaker.media_lib.domain.model.Playlist
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

open class AddPlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.insertPlaylist(playlist)
        }
    }

    fun saveImageToPrivateStorage(uri: Uri, context: Context): String? {
        val picturesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val albumDir = File(picturesDir, "MyAlbum")
        if (!albumDir.exists()) {
            albumDir.mkdirs()
        }
        val destFile = File(albumDir, "cover_${System.currentTimeMillis()}.jpg")

        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(destFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            Log.d("SaveImage", "Изображение сохранено: ${destFile.absolutePath}")
            return destFile.absolutePath
        } catch (e: Exception) {
            Log.e("SaveImage", "Ошибка сохранения изображения", e)
        }
        return null
    }
}