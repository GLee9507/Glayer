package com.gene.libglayer.state

import android.os.Bundle
import android.os.Message
import com.gene.libglayer.*

class PauseState(core: StateMachineCore) : State(core) {
    override fun enter(bundle: Bundle?) {
        super.enter(bundle)
        core.controller.callback { it.onPlayStateChanged(PLAY_STATE_PAUSE) }
    }

    override fun quit() {
        super.quit()
    }

    override fun handle(msg: Message) = when (msg.what) {
        CTRL_PLAY_OR_PAUSE -> consume { core.controller.play() }
        CTRL_NEXT -> consume { core.controller.next() }
        CTRL_PRE -> consume { core.controller.pre() }
        CTRL_MOVE_TO -> consume { core.controller.moveTo(msg.data) }
        CTRL_SEEK_TO -> consume { core.controller.seekTo(msg.data) }
        else -> false
    }
}