package com.mainp.musicapp.presentation.ui.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mainp.musicapp.data.entity.FavoriteSong
import com.mainp.musicapp.data.entity.Song
import com.mainp.musicapp.databinding.ItemSongBinding
import com.mainp.musicapp.presentation.ui.activity.PlayingNowActivity

@androidx.media3.common.util.UnstableApi


class SongAdapter(
    private var songs: List<Song> = listOf(),
) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    private var onItemClickListener: ((Song) -> Unit)? = null

    fun setOnItemClickListener(listener: (Song) -> Unit) {
        onItemClickListener = listener
    }

    class ViewHolder(var binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSongBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songs[position]
        holder.binding.song = song

        Glide.with(holder.itemView.context)
            .load(song.thumbnail)
            .into(holder.binding.imgThumbnail)

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(song)
            Log.d("SongAdapter", "Clicked song: ${song.title}, URL: ${song.path}")
        }
    }

    override fun getItemCount(): Int = songs.size

    fun submitList(newItems: List<Song>) {
        songs = newItems
        notifyDataSetChanged()
    }

    fun getCurrentSongs(): List<Song> {
        return songs
    }
}
