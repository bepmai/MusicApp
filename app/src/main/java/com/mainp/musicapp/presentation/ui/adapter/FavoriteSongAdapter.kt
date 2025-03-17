package com.mainp.musicapp.presentation.ui.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mainp.musicapp.data.entity.FavoriteSong
import com.mainp.musicapp.data.entity.Song
import com.mainp.musicapp.databinding.ItemFavoriteSongBinding
import com.mainp.musicapp.databinding.ItemSongBinding
import com.mainp.musicapp.presentation.ui.activity.PlayingNowActivity

@androidx.media3.common.util.UnstableApi


class FavoriteSongAdapter(
    private var favoriteSongs: List<FavoriteSong> = listOf(),
) : RecyclerView.Adapter<FavoriteSongAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemFavoriteSongBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFavoriteSongBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favoriteSong = favoriteSongs[position]

        holder.binding.favoriteSong = favoriteSong
        holder.binding.executePendingBindings()

        Glide.with(holder.itemView.context)
            .load(favoriteSong.thumbnail)
            .into(holder.binding.imgThumbnail)
    }

    override fun getItemCount(): Int = favoriteSongs.size

    fun submitList(newItems: List<FavoriteSong>) {
        favoriteSongs = newItems
        notifyDataSetChanged()
    }
}
