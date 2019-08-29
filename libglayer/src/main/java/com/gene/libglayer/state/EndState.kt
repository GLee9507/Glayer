package com.gene.libglayer.state

import android.os.Bundle
import android.os.Message

class EndState(core: StateMachineCore) : State(core) {
    override fun enter(bundle: Bundle?) {
//        core.controllerext()
        super.enter(bundle)
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun quit() {
        super.quit()
    }

    override fun handle(msg: Message) = false
}