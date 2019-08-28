package com.gene.libglayer

import android.os.Bundle
import androidx.core.util.Pools
object BundlePool {
        private val pool = Pools.SimplePool<Bundle>(30)

    fun take() = pool.acquire() ?: Bundle()


    fun release(bundle: Bundle) {
        pool.release(bundle)
    }

}