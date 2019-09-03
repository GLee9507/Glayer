package com.gene.glayer.ui.all

import android.media.MediaMetadataRetriever
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.doOnDetach
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gene.glayer.R
import com.gene.glayer.net.NET
import com.gene.libglayer.IO
import com.gene.libglayer.UI
import com.gene.libglayer.model.Media
import com.gene.libglayer.withIoContext
import kotlinx.coroutines.*

val MEDIA_CALLBACK by lazy {
    object : DiffUtil.ItemCallback<Media>() {
        override fun areItemsTheSame(oldItem: Media, newItem: Media) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Media, newItem: Media) = false
    }

}

class AllMediaAdapter : ListAdapter<Media, AllMediaViewHolder>(MEDIA_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AllMediaViewHolder(
            LayoutInflater.from(parent.context!!).inflate(
                R.layout.item_media,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: AllMediaViewHolder, position: Int) {
        val media = currentList[position]
        holder.tvSinger.text = media.artist
        holder.tvSong.text = media.title
        holder.data = media
        holder.job?.cancel()
        holder.job = UI.launch {
            val bitMap = getBitMap(media.data)
            if (bitMap == null) {
                try {
                    val albumInfo = NET.getAlbumInfo(media.artist, media.album)
                    val image = albumInfo.album.image
                    val text = image.lastOrNull {
                        !TextUtils.isEmpty(it.text)
                    }?.text
                    if (TextUtils.isEmpty(text)) {
                        return@launch
                    }
                    Glide.with(holder.ivAlbum)
                        .load(text)
                        .into(holder.ivAlbum).clearOnDetach()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else
                Glide.with(holder.ivAlbum).load(bitMap).into(holder.ivAlbum).clearOnDetach()
        }
    }

    override fun onViewRecycled(holder: AllMediaViewHolder) {
        super.onViewRecycled(holder)
        Glide.with(holder.ivAlbum).clear(holder.ivAlbum)
        holder.job?.cancel()
    }

    private suspend fun getBitMap(uri: String?): ByteArray? {
        if (uri == null) return null
        return withIoContext {
            Log.d("bitmap", Thread.currentThread().name)
            return@withIoContext MediaMetadataRetriever().let {
                it.setDataSource(uri)
                return@let try {
                    it.embeddedPicture
                    null
                } finally {
                    it.release()
                }
            }
        }
    }
}


class AllMediaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    init {
        view.setOnClickListener {
            data?.apply {
            }
        }
    }

    var job: Job? = null
    var data: Media? = null
    val tvSinger: TextView by lazy { view.findViewById<TextView>(R.id.tvSingerTitle) }
    val tvSong: TextView by lazy { view.findViewById<TextView>(R.id.tvSongTitle) }
    val ivAlbum: ImageView by lazy { view.findViewById<ImageView>(R.id.ivAlbum) }


}