package com.gene.libglayer.state

import android.os.Bundle
import android.os.Message
import com.gene.libglayer.NO_MEDIA_ID
import com.gene.libglayer.PLAY_STATE_IDLE

class IdleState(core: StateMachineCore) : State(core) {
    override fun enter(bundle: Bundle?) {
        core.controller.callback {
            it.onPlayMediaChanged(NO_MEDIA_ID)
            it.onPlayStateChanged(PLAY_STATE_IDLE)
        }
    }

    override fun quit() {
    }

    override fun handle(msg: Message): Boolean {
        return false
    }


}