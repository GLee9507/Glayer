package com.gene.glayer.net

import android.util.Log
import com.gene.libglayer.IO
import com.gene.libglayer.TP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.internal.threadFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.*

val CLIENT by  lazy {   OkHttpClient.Builder()
    .dispatcher(
        Dispatcher(
            TP
        )
    )
    .build() }
val NET: Api by lazy {
    Retrofit.Builder().baseUrl("http://ws.audioscrobbler.com/2.0/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(
           CLIENT
        )
        .build().create(Api::class.java)
}
const val API_KTY = "828057baecef147447357d8b4d26e443&"

interface Api {
    @GET("?method=album.getinfo&api_key=$API_KTY&format=json")
    suspend fun getAlbumInfo(@Query("artist") artist: String? = "", @Query("album") album: String? = ""): AlbumInfo
}


