package com.gene.glayer.service

import com.gene.glayer.EventListener

data class EventListenerWrapper(val uuid: String, var listener: EventListener, var version: Int = 0)