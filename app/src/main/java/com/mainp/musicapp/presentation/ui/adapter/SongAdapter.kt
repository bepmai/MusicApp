package com.mainp.musicapp.presentation.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mainp.musicapp.data.entity.Song
import com.mainp.musicapp.databinding.ItemSongBinding
import com.mainp.musicapp.presentation.ui.activity.DemoActivity
@androidx.media3.common.util.UnstableApi


class SongAdapter(
    private var songs: List<Song> = listOf(),
//    private val onItemClick: (Song) -> Unit
) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

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
            val intent = Intent(it.context, DemoActivity::class.java).apply {
                putExtra("title", song.title)
                putExtra("artist", song.artist)
                putExtra("imageUrl", song.thumbnail)
                putExtra("songUrl", song.path)
            }
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = songs.size

    fun submitList(newItems: List<Song>) {
        songs = newItems
        notifyDataSetChanged()
    }
}
