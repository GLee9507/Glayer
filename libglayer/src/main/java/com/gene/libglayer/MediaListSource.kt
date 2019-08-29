package com.gene.libglayer

import android.net.Uri
import com.gene.libglayer.model.Media
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ShuffleOrder

class MediaListSource(
    val tag: String,
    isAtomic: Boolean,
    useLazyPreparation: Boolean,
    shuffleOrder: ShuffleOrder,
    val mediaListMap: MediaListMap
) : ConcatenatingMediaSource(
    isAtomic,
    useLazyPreparation,
    shuffleOrder,
    *Array(mediaListMap.size()) {
        dataSourceFactory.createMediaSource(
            Uri.parse(
                mediaListMap.getAt(
                    it
                ).data
            )
        )
    }
) {

}