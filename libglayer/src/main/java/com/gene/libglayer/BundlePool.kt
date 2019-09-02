package com.gene.libglayer

import android.os.Bundle
import androidx.core.util.Pools

object BundlePool {
    private val pool = Pools.SimplePool<Bundle>(30)

    fun take(what: Int): Bundle {

        val bundle = pool.acquire() ?: Bundle()
        bundle.putInt(WHAT, what)
        return bundle
    }


    fun release(bundle: Bundle) {
        bundle.clear()
        pool.release(bundle)
    }

}