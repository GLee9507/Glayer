package com.gene.glayer.ui

import com.gene.libglayer.model.Media

data class AlbumBean(
    private val media: Media
) {
    val album: String = media.album.toString()

    val artist: String = media.artist.toString()

    val mediaList = ArrayList<Media>().apply { add(media) }
}