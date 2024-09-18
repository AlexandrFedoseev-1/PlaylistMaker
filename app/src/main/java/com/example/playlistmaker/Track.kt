package com.example.playlistmaker


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    @SerializedName("trackId") val trackId: String,
    @SerializedName("trackName")val trackName: String,
    @SerializedName("artistName")val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: String,
    @SerializedName("artworkUrl100")val artworkUrl100: String,
    @SerializedName("collectionName")val collectionName: String?,
    @SerializedName("releaseDate")val releaseDate: String?,
    @SerializedName("primaryGenreName")val primaryGenreName: String?,
    @SerializedName("country")val country: String?
) : Parcelable
