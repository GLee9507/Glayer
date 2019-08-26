package com.gene.glayer

import android.util.SparseArray
import com.gene.glayer.model.Media

class MediaListMap : MutableIterable<Media> {

    override fun iterator() = list.iterator()
    private val map: SparseArray<Media> by lazy { SparseArray<Media>() }
    val list: ArrayList<Media> by lazy { ArrayList<Media>() }

    constructor()

    constructor(mediaList: List<Media>) {
        list.addAll(mediaList)
        mediaList.forEach {
            map.put(it.id, it)
        }
    }

    fun add(media: Media) {
        map.put(media.id, media)
        list.add(media)
    }

    fun size() = list.size

    fun getAt(index: Int): Media = list[index]
    fun getById(id: Int): Media = map[id]

    fun remove(index: Int) {
        list.removeAt(index)
        map.remove(index)
    }

    fun clear() {
        list.clear()
        map.clear()
    }
}