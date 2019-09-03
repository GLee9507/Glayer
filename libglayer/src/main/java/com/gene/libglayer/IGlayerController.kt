package com.gene.libglayer

import android.os.Bundle

interface IController {
    fun play()
    fun pause()
    fun stop()
    fun reset()
    fun seekTo(bundle: Bundle)
    fun next()
    fun pre()
    fun moveTo(bundle: Bundle)
    fun setPlayList(bundle: Bundle)
    fun callback(function: (EventListener) -> Unit)
    fun scan(bundle: Bundle)
    fun currentPosition(): Long
    fun currentIndex(): Int
    fun getCurrentSourceUri(): String?
}