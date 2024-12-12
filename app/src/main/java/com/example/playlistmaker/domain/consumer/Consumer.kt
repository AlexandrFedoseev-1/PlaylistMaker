package com.example.playlistmaker.domain.consumer

interface Consumer<T> {
    fun consume(data: ConsumerData<T>)
}
