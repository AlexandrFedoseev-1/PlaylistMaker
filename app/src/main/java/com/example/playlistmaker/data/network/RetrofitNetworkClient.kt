package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val trackBaseUrl = "https://itunes.apple.com"

    private val retrofit =
        Retrofit.Builder().baseUrl(trackBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val trackService = retrofit.create(TrackSearchApi::class.java)

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
}