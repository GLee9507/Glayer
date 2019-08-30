package com.gene.libglayer.state

import android.os.Bundle
import android.os.Message

class EndState(core: StateMachineCore) : State(core) {
    override fun enter(bundle: Bundle?) {
        super.enter(bundle)
    }

    override fun quit() {
        super.quit()
    }

    override fun handle(msg: Message) = false
}