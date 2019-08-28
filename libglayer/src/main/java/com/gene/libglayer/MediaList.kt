package com.gene.libglayer

import android.net.Uri
import com.gene.libglayer.model.Media
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ShuffleOrder

class MediaList(
    name: String,
    isAtomic: Boolean,
    useLazyPreparation: Boolean,
    shuffleOrder: ShuffleOrder,
    mediaArray: Array<Media>
) : ConcatenatingMediaSource(
    isAtomic,
    useLazyPreparation,
    shuffleOrder,
    *Array<MediaSource>(mediaArray.size) {
        dataSourceFactory.createMediaSource(Uri.parse(mediaArray[it].data))
    }) {
}