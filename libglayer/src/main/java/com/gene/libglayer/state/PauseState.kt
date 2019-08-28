package com.gene.libglayer.state

import android.os.Bundle
import android.os.Message
import com.gene.libglayer.*

class PauseState(core: StateMachineCore) : State(core) {
    override fun enter(bundle: Bundle?) {
        core.controller.callback { it.onPlayStateChanged(PLAY_STATE_PAUSE) }
    }

    override fun quit() {
    }

    override fun handle(msg: Message) = when (msg.what) {
        CTRL_PLAY -> consume { core.controller.play() }
        CTRL_PAUSE -> consume { core.controller.play() }
        CTRL_MOVE_TO -> consume { core.controller.moveTo(msg.data) }
        CTRL_SEEK_TO -> consume { core.controller.seekTo(msg.data) }
        else -> false
    }
}