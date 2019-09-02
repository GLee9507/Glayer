package com.gene.glayer.net

import android.util.Log
import com.gene.glayer.ui.AlbumBean
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.logging.LogManager

val NET: Api by lazy {
    Retrofit.Builder().baseUrl("http://ws.audioscrobbler.com/2.0/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()

                .addNetworkInterceptor {
                    val request = it.request()
                    Log.d("network",request.url().toString())
                    it.proceed(request)
                }.build()
        )

        .build().create(Api::class.java)
}
const val API_KTY = "828057baecef147447357d8b4d26e443&"

interface Api {
    @GET("?method=album.getinfo&api_key=$API_KTY&format=json")
    suspend fun getAlbumInfo(@Query("artist") artist: String, @Query("album") album: String): AlbumInfo
}


