package com.gene.glayer.ui.all

import android.media.MediaMetadataRetriever
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
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.gene.glayer.R
import com.gene.libglayer.APP
import com.gene.libglayer.model.Media
import kotlinx.coroutines.*

val MEDIA_CALLBACK by lazy {
    object : DiffUtil.ItemCallback<Media>() {
        override fun areItemsTheSame(oldItem: Media, newItem: Media) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Media, newItem: Media) = false
    }

}

class AllMediaAdapter : ListAdapter<Media, AllMediaViewHolder>(MEDIA_CALLBACK) {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.doOnDetach {

        }
    }

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
        holder.job = CoroutineScope(Dispatchers.Main).async {
            Log.d("g111", position.toString() + "load start")
            val bitMap = getBitMap(media.data!!)
            Log.d("g111", position.toString() + "load end")
            Log.d("g111", position.toString() + "load comp")
        }
        Log.d("g111", position.toString() + "load cancel")
    }

    override fun onViewRecycled(holder: AllMediaViewHolder) {
        super.onViewRecycled(holder)
        Glide.with(holder.ivAlbum).clear(holder.ivAlbum)
        holder.job?.cancel()
    }

    private suspend fun getBitMap(uri: String) =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            Log.d("g111", Thread.currentThread().name + "loading")
            return@withContext MediaMetadataRetriever().apply {
                setDataSource(uri)
            }.embeddedPicture
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