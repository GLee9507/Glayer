package com.gene.libglayer


import android.net.Uri
import android.os.Bundle
import android.os.Message
import androidx.databinding.Observable
import com.gene.libglayer.model.Media
import com.gene.libglayer.state.StateMachineCore
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ShuffleOrder
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import java.util.concurrent.CopyOnWriteArrayList


class GlayerController : IGlayerController.Stub(), IController {


    private val core by lazy { StateMachineCore(this) }

    private val player by lazy {
        ExoPlayerFactory.newSimpleInstance(
            APP,
            DefaultRenderersFactory(APP).apply { setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON) },
            DefaultTrackSelector(), DefaultLoadControl(), null, core.looper
        ).apply { addListener(core) }
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


    override fun registerEventListenerListener(uuid: String, listener: EventListener?) {
        if (listener != null) listenerWrappers.add(
            EventListenerWrapper(
                uuid,
                listener
            )
        )
    }
    override fun currentPosition() = player.currentPosition

    override fun currentIndex() = player.currentWindowIndex

    override fun stop() {
        player.stop()
    }

    override fun reset() {
        player.stop(true)
    }

    override fun scan(bundle: Bundle) {
        mediaRepo.scan(bundle.getString(CTRL_SCAN_PATH))
    }


    override fun moveTo(bundle: Bundle) {
        bundle.getInt(CTRL_MOVE_TO_INDEX, -1).apply {
            if (this >= 0) {
                play()
                player.seekTo(this, 0)
            }

        }
    }

    override fun seekTo(bundle: Bundle) {
        bundle.getLong(CTRL_SEEK_TO_POSITION, -1).apply {
            if (this >= 0) {
                play()
                player.seekTo(this)
            }

        }
    }

    override fun setPlayList(bundle: Bundle) {
        val tag = bundle.getString(CTRL_SET_LIST_TAG, "")
        val playList = bundle.getIntArray(CTRL_SET_LIST_PLAY_LIST)!!
        val autoPlay = bundle.getBoolean(CTRL_SET_LIST_AUTO_PLAY)
        val playIndex = bundle.getInt(CTRL_SET_LIST_PLAY_INDEX)
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


    override fun send(msg: Message) {
        core.sendMessage(msg)
    }

    fun onCleared() {
        mediaRepo.onCleared()
    }


    override fun callback(function: (EventListener) -> Unit) {
        listenerWrappers.forEach {
            function.invoke(it.listener)
        }
    }

    override fun play() {
        player.playWhenReady = true
    }

    override fun pause() {
        player.playWhenReady = false
    }


}