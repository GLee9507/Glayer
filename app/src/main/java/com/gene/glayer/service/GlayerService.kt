package com.gene.glayer.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.util.Log
import com.gene.glayer.APP
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer

class GlayerService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return glayerController
    }

    override fun onCreate() {
        startService(Intent(this,GlayerService::class.java))
        super.onCreate()
    }
    private val glayerController by lazy { GlayerController() }

    override fun onDestroy() {
        super.onDestroy()
        glayerController.onCleared()
    }
}