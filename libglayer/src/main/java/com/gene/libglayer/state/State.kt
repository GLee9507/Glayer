package com.gene.libglayer.state

import android.os.Bundle
import android.os.Message
import androidx.collection.ArrayMap

abstract class State(val core: StateMachineCore) {

    abstract fun enter(bundle: Bundle?)
    abstract fun quit()

    abstract fun handle(msg: Message): Boolean

    companion object {
        private val map = ArrayMap<Class<*>, Any>()
        fun <T> get(clazz: Class<T>, core: StateMachineCore): T {
            var state = map[clazz]
            if (state == null) {
                val declaredConstructor = clazz.getDeclaredConstructor(StateMachineCore::class.java)
                state = declaredConstructor.newInstance(core)
                map[clazz] = state
            }
            return state as T
        }
    }
}