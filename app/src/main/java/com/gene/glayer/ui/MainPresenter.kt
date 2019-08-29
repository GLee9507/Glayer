package com.gene.glayer.ui

import android.os.Bundle
import android.os.Message
import com.gene.libglayer.*

class MainPresenter(private val viewModel: MainViewModel) {
    fun playOrPause() {
        send(BundlePool.take(CTRL_PLAY_OR_PAUSE))
    }

    fun next() {
        send(BundlePool.take(CTRL_NEXT))
    }

    fun pre() {
        send(BundlePool.take(CTRL_PRE))
    }

    fun setPlayList() {
        send(BundlePool.take(CTRL_SET_LIST).apply {
            putString(CTRL_SET_LIST_TAG, "test")
            putIntArray(CTRL_SET_LIST_PLAY_LIST,
                viewModel.mediaList.value?.size()?.let { it ->
                    IntArray(it, init = { i ->
                        viewModel.mediaList.value!!.getAt(i).id
                    })
                })

            putBoolean(CTRL_SET_LIST_AUTO_PLAY, true)
            putInt(CTRL_SET_LIST_PLAY_INDEX, 0)
        })
    }

    private fun send(data: Bundle) {
        viewModel.glayerController?.apply { this.send(data) }
    }
}