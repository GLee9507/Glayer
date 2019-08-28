package com.gene.libglayer


data class EventListenerWrapper(val uuid: String, var listener: EventListener, var version: Int = 0)