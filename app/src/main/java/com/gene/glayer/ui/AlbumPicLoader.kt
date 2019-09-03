package com.gene.glayer.ui

import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.RequestBuilder
import com.gene.glayer.net.GlideApp
import com.gene.glayer.net.NET
import com.gene.libglayer.APP
import com.gene.libglayer.UI
import com.gene.libglayer.isEmptyOrNull
import com.gene.libglayer.model.Media
import com.gene.libglayer.withIoContext
import kotlinx.coroutines.launch

object AlbumPicLoader {
    fun load(media: Media, view: ImageView) = UI.launch {
        val data = media.data ?: return@launch
        val any = withIoContext {
            Log.d("load-start", media.data + "-" + view.toString())
            val bitmapArray = MediaMetadataRetriever().let {
                it.setDataSource(data)
                return@let try {
                    it.embeddedPicture
                } finally {
                    it.release()
                }
            }
            if (bitmapArray != null && bitmapArray.isNotEmpty()) {
                return@withIoContext bitmapArray as Any
            } else {
                val albumInfo = NET.getAlbumInfo(media.artist, media.album)
                albumInfo.album.image.last { image ->
                    !image.text.isEmptyOrNull()
                }.text
            }
        }
        Log.d("load-end", media.data + "-" + view.toString())
//        val load = GlideApp.with(view).load(any)
        GlideApp.with(view).load(any).into(view)
    }
}
