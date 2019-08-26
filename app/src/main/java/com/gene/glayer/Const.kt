package com.gene.glayer

import android.app.Application
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.FileDataSourceFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

const val PLAY_STATE_PLAYING = 1
const val PLAY_STATE_PAUSE = 2
const val PLAY_STATE_IDEL = 3


const val CALLBACK_PROGRESS = -11
const val UNKNOWN_ID = -1

val APP: Application by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    AppHolder.getContext()

}
val dataSourceFactory by lazy { ProgressiveMediaSource.Factory(FileDataSourceFactory()) }


val IO by lazy { CoroutineScope(Dispatchers.IO) }


fun runOnMain(block: Runnable) {
    mainHandler.post(block)
}

inline fun runOnMain(crossinline block: () -> Unit) {
    mainHandler.post { block.invoke() }
}

val mainHandler by lazy { Handler(Looper.getMainLooper()) }