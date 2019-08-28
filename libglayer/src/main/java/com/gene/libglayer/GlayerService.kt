package com.gene.libglayer

import android.app.Service
import android.content.Intent
import android.os.IBinder

class GlayerService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return glayerController
    }

    override fun onCreate() {
        startService(Intent(this, GlayerService::class.java))
        super.onCreate()
    }
    private val glayerController by lazy { GlayerController() }

    override fun onDestroy() {
        super.onDestroy()
        glayerController.onCleared()
    }
}