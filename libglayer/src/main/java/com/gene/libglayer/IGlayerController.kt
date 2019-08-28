package com.gene.libglayer

import android.os.Bundle
import android.util.Log

interface IController {
    fun play()
    fun pause()
    fun stop()
    fun reset()
    fun seekTo(bundle: Bundle)
    fun moveTo(bundle: Bundle)
    fun setPlayList(bundle: Bundle)
    fun callback(function: (EventListener) -> Unit)
    fun scan(bundle: Bundle)
    fun currentPosition():Long
    fun currentIndex():Int
}