package com.gene.libglayer.state

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import com.gene.libglayer.CTRL_SCAN
import com.gene.libglayer.CTRL_SET_LIST
import com.gene.libglayer.IController
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.source.MediaSourceEventListener
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray


class StateMachineCore(val controller: IController) :

    Handler(
        HandlerThread("glayer-core-thread", 1000).apply {
            start()
        }.looper
    ), Player.EventListener, AnalyticsListener {

    private var currentState: State = State.get(IdleState::class.java, this).apply { enter(null) }

    override fun handleMessage(msg: Message) {
        if (!currentState.handle(msg)) when (msg.what) {
            CTRL_SCAN -> controller.scan(msg.data)
            CTRL_SET_LIST -> controller.setPlayList(msg.data)
        }
    }

    private var currentUriString: String? = null
    override fun onLoadStarted(
        eventTime: AnalyticsListener.EventTime?,
        loadEventInfo: MediaSourceEventListener.LoadEventInfo?,
        mediaLoadData: MediaSourceEventListener.MediaLoadData?
    ) {
        val uriString = loadEventInfo!!.uri.toString()
        currentUriString = uriString
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {

        Log.d("onPlayerStateChanged", "$playWhenReady--$playbackState")
        when (playbackState) {
            Player.STATE_IDLE -> {
//                transformTo(IdleState::class.java, null)
            }
            Player.STATE_READY -> {
                if (playWhenReady) {
                    transformTo(
                        PlayingState::class.java,
                        Bundle().apply { putString("uri", currentUriString) })
                } else {
                    transformTo(PauseState::class.java, null)
                }
            }
            Player.STATE_ENDED -> {
                transformTo(EndState::class.java, null)
            }
            Player.STATE_BUFFERING -> {
            }
        }
    }

    private fun <T> transformTo(clazz: Class<T>, bundle: Bundle?) where T : State {
        if (!currentState::class.java.isAssignableFrom(clazz)) {
            currentState.quit()
        }
        currentState = State.get(clazz, this).apply { enter(bundle) }
    }


}