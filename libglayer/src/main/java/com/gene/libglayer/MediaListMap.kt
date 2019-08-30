package com.gene.libglayer

import android.net.Uri
import android.util.SparseArray
import androidx.collection.ArrayMap
import com.gene.libglayer.model.Media

class MediaListMap : MutableIterable<Media> {

    override fun iterator() = list.iterator()
    //    private val map: SparseArray<Media> by lazy { SparseArray<Media>() }
    val list: ArrayList<Media> by lazy { ArrayList<Media>() }
    private var dataList: ArrayList<String>? = null
    private val map = ArrayMap<String, Media>()

    constructor()

    constructor(mediaList: Array<Media>) {
        list.addAll(mediaList)
        mediaList.forEach {
            //            map.put(it.id, it)
            map[it.data] = it
            dataList?.add(it.data!!)
        }
    }

    fun add(media: Media) {
        map[media.data] = media
        list.add(media)
        dataList?.add(media.data!!)
    }

    fun size() = list.size
    fun getDataList(): ArrayList<String> {
        if (dataList == null) {
            dataList = ArrayList<String>(list.size).apply {
                list.forEach {
                    add(it.data!!)
                }
            }
        }
        return dataList!!
    }

    fun getAt(index: Int): Media = list[index]
    fun get(data: String): Media? = map[data]


    fun remove(index: Int) {
        map.remove(list.removeAt(index).data)
        dataList?.removeAt(index)
    }

    fun clear() {
        dataList?.clear()
        list.clear()
        map.clear()
    }
}