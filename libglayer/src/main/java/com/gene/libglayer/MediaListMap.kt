package com.gene.libglayer

import android.util.SparseArray
import com.gene.libglayer.model.Media

class MediaListMap : MutableIterable<Media> {

    override fun iterator() = list.iterator()
    private val map: SparseArray<Media> by lazy { SparseArray<Media>() }
    val list: ArrayList<Media> by lazy { ArrayList<Media>() }
    val idList: ArrayList<Int> by lazy { ArrayList<Int>() }

    constructor()

    constructor(mediaList: Array<Media>) {
        list.addAll(mediaList)
        mediaList.forEach {
            map.put(it.id, it)
            idList.add(it.id)
        }
    }

    fun add(media: Media) {
        map.put(media.id, media)
        list.add(media)
        idList.add(media.id)
    }

    fun size() = list.size

    fun getAt(index: Int): Media = list[index]
    fun getById(id: Int): Media = map[id]

    fun remove(index: Int) {
        list.removeAt(index)
        map.remove(index)
        idList.removeAt(index)
    }

    fun clear() {
        idList.clear()
        list.clear()
        map.clear()
    }
}