package com.gene.libglayer.state

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import com.gene.libglayer.CTRL_SCAN
import com.gene.libglayer.CTRL_SET_LIST
import com.gene.libglayer.IController
import com.gene.libglayer.consume
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline


class StateMachineCore(val controller: IController) :

    Handler(
        HandlerThread("glayer-core-thread", 1000).apply {
            start()
        }.looper
    ), Player.EventListener {

    private var currentState: State = State.get(IdleState::class.java, this).apply { enter(null) }

    override fun handleMessage(msg: Message) {
        if (!currentState.handle(msg)) when (msg.what) {
            CTRL_SCAN -> controller.scan(msg.data)
            CTRL_SET_LIST -> controller.setPlayList(msg.data)
        }
    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {

    }


    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {

        Log.d("onPlayerStateChanged" , "$playWhenReady--$playbackState")
        when (playbackState) {
            Player.STATE_IDLE -> {
                transformTo(IdleState::class.java, null)
            }
            Player.STATE_READY -> {
                if (playWhenReady) {
                    transformTo(PlayState::class.java, null)
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
        currentState.quit()
        currentState = State.get(clazz, this).apply { enter(bundle) }
    }


}