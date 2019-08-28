package com.gene.libglayer.state

import android.os.Bundle
import android.os.Message
import com.gene.libglayer.PLAY_STATE_IDEL

class IdleState(core: StateMachineCore) : State(core) {
    override fun enter(bundle: Bundle?) {
        core.controller.callback { it.onPlayStateChanged(PLAY_STATE_IDEL) }
    }

    override fun quit() {
    }

    override fun handle(msg: Message): Boolean {
        return false
    }


}