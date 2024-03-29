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

    fun seek(progress: Int) {
        send(BundlePool.take(CTRL_SEEK_TO).apply { putInt(CTRL_SEEK_TO_POSITION, progress) })
    }

    fun setPlayList() {
        send(BundlePool.take(CTRL_SET_LIST).apply {
            putString(CTRL_SET_LIST_TAG, "test")
            putStringArray(CTRL_SET_LIST_PLAY_LIST,
                viewModel.mediaList.value?.size()?.let { it ->
                    Array(it, init = { i ->
                        viewModel.mediaList.value!!.getAt(i).data!!
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