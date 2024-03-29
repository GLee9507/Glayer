package com.gene.libglayer


import android.os.Bundle
import android.util.Log
import androidx.databinding.Observable
import com.gene.libglayer.model.Media
import com.gene.libglayer.state.StateMachineCore
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player.REPEAT_MODE_ALL
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ShuffleOrder
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.TransferListener
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.CopyOnWriteArrayList
import java.util.logging.LogManager


class GlayerController : IGlayerController.Stub(), IController {


    private val core by lazy { StateMachineCore(this) }

    private val player by lazy {
        ExoPlayerFactory.newSimpleInstance(
            APP,
            DefaultRenderersFactory(APP).apply { setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON) },
            DefaultTrackSelector(),
            DefaultLoadControl.Builder().setPrioritizeTimeOverSizeThresholds(false)
                .createDefaultLoadControl(),
            null,
            core.looper
        ).apply {
            addListener(core)
            addAnalyticsListener(core)
            repeatMode = REPEAT_MODE_ALL
        }
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
                    element.listener.onAllListChanged(mediaRepo.get()?.list?.toTypedArray())
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
                            listenerWrapper.listener.onAllListChanged(list.toTypedArray())
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
    override fun getCurrentSourceUri(): String? {
        return currentMediaList?.mediaListMap?.getAt(currentIndex())?.data
    }

    override fun stop() {
        player.stop()
    }

    override fun reset() {
        player.stop(true)
    }

    override fun scan(bundle: Bundle) {
        mediaRepo.scan(bundle.getString(CTRL_SCAN_PATH))
    }

    override fun next() {
        player.next()
    }

    override fun pre() {
        player.previous()
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

        bundle.getInt(CTRL_SEEK_TO_POSITION, -1).apply {
            if (this >= 0) {
                play()
                player.seekTo(this.toLong())
            }

        }
    }

    private var currentMediaList: MediaListSource? = null
    override fun setPlayList(bundle: Bundle) {
        player.isPlayingAd
        val tag = bundle.getString(CTRL_SET_LIST_TAG, "")
        val playList = bundle.getStringArray(CTRL_SET_LIST_PLAY_LIST)!!
        val autoPlay = bundle.getBoolean(CTRL_SET_LIST_AUTO_PLAY)
        val playIndex = bundle.getInt(CTRL_SET_LIST_PLAY_INDEX)
        mediaRepo.get()?.apply {

            val mediaList = MediaListSource(
                tag,
                isAtomic = true,
                useLazyPreparation = false,
                shuffleOrder = ShuffleOrder.DefaultShuffleOrder(0),
                mediaListMap = MediaListMap(
                    ArrayList<Media>().apply {
                        playList.filter {
                            File(it).exists()
                        }.forEach { uriString ->
                            get(uriString)?.apply {
                                add(this)
                            }
                        }
                    }.toTypedArray()
                )
            )
            player.playWhenReady = autoPlay
            player.prepare(mediaList, true, true)
//            player.seekTo(playIndex, 0)
//            player.playWhenReady = autoPlay
            currentMediaList = mediaList
            player.addAnalyticsListener(core)
        }
    }


    override fun send(data: Bundle) {
        core.sendMessage(core.obtainMessage(data.getInt(WHAT)).apply { this.data = data })
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