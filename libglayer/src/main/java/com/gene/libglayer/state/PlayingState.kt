package com.gene.libglayer.state

import android.os.Bundle
import android.os.Message
import android.util.Log
import com.gene.libglayer.*


class PlayingState(core: StateMachineCore) : State(core) {

    override fun enter(bundle: Bundle?) {
        super.enter(bundle)
        Log.d("PlayingState", bundle.toString())
        core.sendEmptyMessageDelayed(CALLBACK_PROGRESS, 0)
        core.controller.callback {
            it.onPlayStateChanged(PLAY_STATE_PLAYING)
            bundle?.getString("uri")?.apply {
                it.onPlayMediaChanged(this)
            }
        }
    }

    override fun handle(msg: Message) = when (msg.what) {
        CALLBACK_PROGRESS -> consume {
            core.controller.callback { it.onProgressChanged(core.controller.currentPosition()) }
            core.sendEmptyMessageDelayed(CALLBACK_PROGRESS, 1000)
        }
        CTRL_NEXT -> consume { core.controller.next() }
        CTRL_PRE -> consume { core.controller.pre() }
        CTRL_PLAY_OR_PAUSE -> consume { core.controller.pause() }
        CTRL_MOVE_TO -> consume { core.controller.moveTo(msg.data) }
        CTRL_SEEK_TO -> consume { core.controller.seekTo(msg.data) }
        else -> false
    }


    override fun quit() {
        super.quit()
        core.removeMessages(CALLBACK_PROGRESS)
    }

    companion object {
        const val CALLBACK_PROGRESS = -11
    }
}