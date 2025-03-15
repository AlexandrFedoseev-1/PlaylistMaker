package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class RetrofitNetworkClient(private val trackService: TrackSearchApi) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (dto !is TrackRequest) {
            return Response().apply { resultCode = 400 }
        }
        return withContext(Dispatchers.IO) {
            try {
                val resp = trackService.searchTracks(dto.expression)
                resp.apply { resultCode = 200 }

            } catch (ex: Exception) {
                Response().apply { resultCode = 500 }
            }
        }

    }

    companion object {
        const val TRACK_BASE_URL = "https://itunes.apple.com"
    }
}
