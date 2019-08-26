package com.gene.glayer.ui.all

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gene.glayer.R
import com.gene.glayer.model.Media
import com.gene.glayer.ui.GlayerClient

class AllMediaAdapter : ListAdapter<Media, AllMediaViewHolder>(Media.ITEM_CALLBACK) {
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
    }

}

class AllMediaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    init {
        view.setOnClickListener {
            data?.apply {
                GlayerClient.prepare(id)
            }
        }
    }

    var data: Media? = null
    val tvSinger: TextView by lazy { view.findViewById<TextView>(R.id.tvSingerTitle) }
    val tvSong: TextView by lazy { view.findViewById<TextView>(R.id.tvSongTitle) }
}