package com.gene.glayer.ui

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gene.libglayer.*
import com.gene.libglayer.model.Media

class MainViewModel : ViewModel(), ServiceConnection {

    val mediaList by lazy { MutableLiveData<MediaListMap>() }
    val playState by lazy { MutableLiveData<Int>() }
    val playProgress by lazy { MutableLiveData<Int>() }
    val playMedia by lazy { MutableLiveData<Media>() }
    val presenter by lazy { MainPresenter(this) }
    private val eventListener by lazy {
        object : EventListener.Stub() {
            override fun onAllListChanged(medialist: Array<Media>?) {
                mediaList.postValue(
                    if (medialist == null) MediaListMap() else MediaListMap(mediaList = medialist)
                )
            }

            override fun onPlayStateChanged(state: Int) {
                playState.postValue(state)
            }


            override fun onPlayMediaChanged(id: Int) {
                mediaList.value?.apply {
                    val media = getById(id)
                    playMedia.postValue(media)
                }
            }

            override fun onProgressChanged(progress: Long) {
                playProgress.postValue(progress.toInt())
            }
        }
    }
    internal var glayerController: IGlayerController? = null
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

        glayerController = object : IGlayerController.Default() {

            val binder = IGlayerController.Stub.asInterface(service)

            override fun registerEventListenerListener(uuid: String?, listener: EventListener?) {
                binder.registerEventListenerListener(uuid, listener)
            }

            override fun send(data: Bundle?) {
                binder.send(data)
                data?.let {
                    BundlePool.release(it)
                }
            }

        }

        glayerController?.registerEventListenerListener(
            APP.packageName.toString(),
            eventListener
        )


    }

    override fun onServiceDisconnected(name: ComponentName?) {
        glayerController = null
    }


    init {
        APP.bindService(
            Intent(
                APP,
                GlayerService::class.java
            ), this, Service.BIND_AUTO_CREATE
        )
    }

    override fun onCleared() {
        APP.unbindService(this)
        super.onCleared()
    }


}