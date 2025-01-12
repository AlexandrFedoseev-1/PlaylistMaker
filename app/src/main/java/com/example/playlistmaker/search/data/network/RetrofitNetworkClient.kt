package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val trackService: TrackSearchApi) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        try {
            if (dto is TrackRequest){

                val resp = trackService.searchTracks(dto.expression).execute()

                val body = resp.body() ?: Response()

                return body.apply { resultCode  = resp.code() }
            }else{
                return Response().apply { resultCode = 400 }
            }
        }catch (ex: Exception){
            return Response().apply { resultCode = 500 }
        }
    }
companion object{
    const val TRACK_BASE_URL = "https://itunes.apple.com"
}
}
