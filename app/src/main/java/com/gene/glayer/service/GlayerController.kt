package com.gene.glayer.service


import android.net.Uri
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import androidx.databinding.Observable
import com.gene.glayer.*
import com.gene.glayer.model.Media
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.flac.FlacLibrary
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ShuffleOrder
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import java.util.concurrent.CopyOnWriteArrayList

class GlayerController : IGlayerController.Stub(), Handler.Callback, Player.EventListener {


    private val glayerHandler by lazy {
        val handlerThread = HandlerThread("glayer_handler")
        handlerThread.start()
        Handler(handlerThread.looper, this)
    }

    private val player by lazy {
        ExoPlayerFactory.newSimpleInstance(
            APP,
            DefaultRenderersFactory(APP).apply { setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON) },
            DefaultTrackSelector(), DefaultLoadControl(), null, glayerHandler.looper
        ).apply { addListener(this@GlayerController) }
    }


    private val listenerWrappers: CopyOnWriteArrayList<EventListenerWrapper> by lazy {
        object : CopyOnWriteArrayList<EventListenerWrapper>() {
            override fun add(element: EventListenerWrapper): Boolean {
                for (eventListenerWrapper in this) {
                    if (eventListenerWrapper.uuid == element.uuid) {
                        eventListenerWrapper.listener = element.listener
                        return true
                    }
                }
                val versionCode = this@GlayerController.mediaRepo.versionCode
                if (element.version < versionCode) {
                    element.listener.onPlayListChanged(mediaRepo.get()?.list ?: ArrayList())
                    element.version = versionCode
                }
                return super.add(element)
            }
        }
    }

    private val mediaRepo =
        MediaRepo().apply {
            addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                    val list: MutableList<Media> = this@apply.get()?.list ?: ArrayList()
                    this@apply.versionCode

                    for (listenerWrapper in this@GlayerController.listenerWrappers) {
                        if (listenerWrapper.version < this@apply.versionCode) {
                            listenerWrapper.listener.onPlayListChanged(list)
                            listenerWrapper.version = this@apply.versionCode
                        }
                    }
                }
            })
        }


    init {
        mediaRepo.searchAndUpdate()
    }

    private fun startCallbackProgress() {
        glayerHandler.sendEmptyMessage(CALLBACK_PROGRESS)
    }

    private fun stopCallbackProgress() {
        glayerHandler.removeMessages(CALLBACK_PROGRESS)
    }

    override fun handleMessage(msg: Message): Boolean {
        when (msg.what) {
            CALLBACK_PROGRESS -> {
                listenerWrappers.forEach {
                    it.listener.onProgressChanged(
                        player.currentPosition.toInt(),
                        player.duration.toInt()
                    )
                }
                glayerHandler.sendEmptyMessageDelayed(CALLBACK_PROGRESS, 1000)
            }
        }
        return false
    }

    override fun registerEventListenerListener(uuid: String, listener: EventListener?) {
        if (listener != null) listenerWrappers.add(EventListenerWrapper(uuid, listener))
    }

    override fun scan(path: String?) {
        mediaRepo.scan(path)
    }

    override fun prepare(id: Int) {
        glayerHandler.post {
            player.stop()
            var realId = UNKNOWN_ID
            mediaRepo.get()?.getById(id)?.apply {
                realId = id
                player.playWhenReady = true

                player.prepare(
                    dataSourceFactory.createMediaSource(
                        Uri.parse(data)
                    ),
                    true,
                    true
                )
            }
            listenerWrappers.forEach {
                it.listener.onPlayMediaChanged(realId)
            }
        }
    }

    private val playBlock by lazy { Runnable { player.playWhenReady = true } }
    private val pauseBlock by lazy { Runnable { player.playWhenReady = false } }
    private currentPlayList:
    override fun setPlayList(name: String?, playList: IntArray?) {
        if (name == null || playList == null) return
        glayerHandler.post {
            mediaRepo.get()?.apply {
                val currentList = Array<MediaSource>(playList.size, init = {
                    val id = playList[it]
                    val media = getById(id)
                    dataSourceFactory.createMediaSource(Uri.parse(media.data))
                })
                val mediaSource = ConcatenatingMediaSource(
                    true,
                    true,
                    ShuffleOrder.DefaultShuffleOrder(0),
                    *currentList
                )
            }

        }
    }

    override fun play() {
        glayerHandler.post(playBlock)
    }

    override fun pause() {
        glayerHandler.post(pauseBlock)
    }

    fun onCleared() {
        mediaRepo.onCleared()
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        Log.d("errpr", error?.message)
    }

    override fun onLoadingChanged(isLoading: Boolean) {
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        var state = 0
        if (playbackState == Player.STATE_READY) {
            state = if (playWhenReady) {
                startCallbackProgress()

                PLAY_STATE_PLAYING
            } else {
                stopCallbackProgress()
                PLAY_STATE_PAUSE
            }
        }


        listenerWrappers.forEach {
            it.listener.onPlayStateChanged(state)
        }
    }
}