package com.gene.libglayer.state

import android.os.Bundle
import android.os.Message
import com.gene.libglayer.*

const val CALLBACK_PROGRESS = -11

class PlayState(core: StateMachineCore) : State(core) {

    override fun enter(bundle: Bundle?) {
        core.sendEmptyMessageDelayed(CALLBACK_PROGRESS, 0)
        core.controller.callback { it.onPlayStateChanged(PLAY_STATE_PLAYING) }
    }

    override fun handle(msg: Message) = when (msg.what) {
        CALLBACK_PROGRESS -> consume {
            core.controller.callback { it.onProgressChanged(core.controller.currentPosition()) }
            core.sendEmptyMessageDelayed(CALLBACK_PROGRESS, 1000)
        }

        CTRL_PAUSE -> consume { core.controller.play() }
        CTRL_MOVE_TO -> consume { core.controller.moveTo(msg.data) }
        CTRL_SEEK_TO -> consume { core.controller.seekTo(msg.data) }
        else -> false
    }


    override fun quit() {
        core.removeMessages(CALLBACK_PROGRESS)
    }

}