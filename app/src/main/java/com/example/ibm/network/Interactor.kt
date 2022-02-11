package com.example.ibm.network

import android.os.Handler
import android.os.Looper
import com.example.ibm.model.FilmList
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.concurrent.thread

class Interactor {
    private val playlistApi: PlaylistApi

    init {
        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client: OkHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
        }.build()
        val retrofit = Retrofit.Builder()
            .baseUrl(PlaylistApi.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        this.playlistApi = retrofit.create(PlaylistApi::class.java)
    }

    private fun <T> runCallOnBackgroundThread(
        call: Call<T>,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val handler = Handler(Looper.getMainLooper()!!)
        thread {
            try {
                val response = call.execute().body()!!
                handler.post { onSuccess(response) }

            } catch (e: Exception) {
                e.printStackTrace()
                handler.post { onError(e) }
            }
        }
    }

    fun getSong(
        onSuccess: (FilmList) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val getFilmRequest = playlistApi.getFilm()
        runCallOnBackgroundThread(getFilmRequest, onSuccess, onError)
    }
}