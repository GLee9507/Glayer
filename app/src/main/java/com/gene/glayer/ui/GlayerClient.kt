package com.gene.glayer.ui

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.util.Pools
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.gene.glayer.APP
import com.gene.glayer.IGlayerController
import com.gene.glayer.EventListener
import com.gene.glayer.MediaListMap
import com.gene.glayer.model.Media
import com.gene.glayer.service.GlayerController
import com.gene.glayer.service.GlayerService
import java.time.Duration
import java.util.*

object GlayerClient : IGlayerController.Default(), ServiceConnection {
    val mediaList by lazy { MutableLiveData<MediaListMap>() }
    val mediaState by lazy { MutableLiveData<Int>() }
    val playProgress by lazy { MutableLiveData<Int>() }
    val playDuration by lazy { MutableLiveData<Int>() }
    val playMedia by lazy { MutableLiveData<Media>() }

    private var glayerController: IGlayerController? = null

    private val eventListener by lazy {
        object : EventListener.Stub() {
            override fun onPlayStateChanged(state: Int) {
                mediaState.postValue(state)
            }

            override fun onPlayListChanged(list: MutableList<Media>?) {
                mediaList.postValue(if (list == null) MediaListMap() else MediaListMap(list))
            }

            override fun onPlayMediaChanged(id: Int) {
                mediaList.value?.apply {
                    val media = getById(id)
                    playMedia.postValue(media)
                }
            }

            override fun onProgressChanged(progress: Int, duration: Int) {
                Log.d("gleee", "$progress-$duration")
                playProgress.postValue(progress)
                playDuration.postValue(duration)
            }
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        glayerController = IGlayerController.Stub.asInterface(service)
        Log.d("glee", "onServiceConnected")

        glayerController?.registerEventListenerListener(APP.packageName.toString(), eventListener)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        glayerController = null
        Log.d("glee", "onServiceDisconnected")
    }

    private val intent by lazy { Intent(APP, GlayerService::class.java) }
    override fun play() {
        glayerController?.play()
    }

    override fun pause() {
        glayerController?.pause()
    }


    override fun scan(path: String?) {
        super.scan(path)
    }

    override fun prepare(id: Int) {
        glayerController?.prepare(id)
    }

    fun connect() {
        APP.bindService(intent, this, Service.BIND_AUTO_CREATE)
    }

    fun disconnect() {
        APP.unbindService(this)
        glayerController = null
    }
}

