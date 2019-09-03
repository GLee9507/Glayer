package com.gene.glayer.ui.all

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gene.glayer.R
import com.gene.glayer.ui.AlbumPicLoader
import com.gene.libglayer.model.Media
import kotlinx.coroutines.Job

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
        holder.job = AlbumPicLoader.load(media,holder.ivAlbum)
    }

    override fun onViewRecycled(holder: AllMediaViewHolder) {
        super.onViewRecycled(holder)
        holder.job?.cancel()
        Glide.with(holder.ivAlbum).clear(holder.ivAlbum)
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