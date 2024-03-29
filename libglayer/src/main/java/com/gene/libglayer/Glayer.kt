package com.gene.libglayer

import android.app.Application
import android.os.Handler
import android.os.Looper
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.FileDataSourceFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

const val PLAY_STATE_PLAYING = 1
const val PLAY_STATE_PAUSE = 2
const val PLAY_STATE_IDLE = 3
const val PLAY_STATE_LOADING = 4

const val NO_MEDIA_ID = -1

const val WHAT = "WHAT"
const val CTRL_PLAY_OR_PAUSE = 1
const val CTRL_SCAN = 3
const val CTRL_SCAN_PATH = "ctrl_scan"

const val CTRL_SET_LIST = 4
const val CTRL_SET_LIST_TAG = "tag"
const val CTRL_SET_LIST_PLAY_INDEX = "play_index"
const val CTRL_SET_LIST_AUTO_PLAY = "auto_play"
const val CTRL_SET_LIST_PLAY_LIST = "play_list"

const val CTRL_MOVE_TO = 5
const val CTRL_MOVE_TO_INDEX = "index"

const val CTRL_SEEK_TO = 6
const val CTRL_SEEK_TO_POSITION = "position"

const val CTRL_NEXT = 7
const val CTRL_PRE = 8


const val UNKNOWN_ID = -1

val APP: Application by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    AppHolder.getContext()

}
val dataSourceFactory by lazy {
    ProgressiveMediaSource.Factory(FileDataSourceFactory())
}


val IO by lazy { CoroutineScope(TP.asCoroutineDispatcher()) }
val UI by lazy { CoroutineScope(Dispatchers.Main) }

val TP by lazy {
    ThreadPoolExecutor(
        0, Int.MAX_VALUE, 60, TimeUnit.SECONDS,
        SynchronousQueue(), object : ThreadFactory {
            private val int = AtomicInteger()
            override fun newThread(r: Runnable?) = Thread(r, "glee-thread-" + int.incrementAndGet())

        }
    )
}

suspend fun <T> withIoContext(block: suspend CoroutineScope.() -> T) =
    withContext(IO.coroutineContext, block = block)

inline fun consume(block: () -> Unit): Boolean {
    block()
    return true
}

fun String?.isEmptyOrNull() =
    this == null || this.isEmpty()