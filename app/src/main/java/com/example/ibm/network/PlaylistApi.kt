package com.example.ibm.network

import com.example.ibm.model.FilmList
import retrofit2.Call
import retrofit2.http.GET

interface PlaylistApi {

    companion object {
        const val BASE_URL="https://android-intern-homework.vercel.app/"
    }

    @GET("/api")
    fun getFilm(): Call<FilmList>
}