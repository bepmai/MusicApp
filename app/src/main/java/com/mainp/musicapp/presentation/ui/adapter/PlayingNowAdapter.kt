package com.mainp.musicapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mainp.musicapp.data.entity.Song
import com.mainp.musicapp.databinding.ItemAlbumBinding
import com.mainp.musicapp.databinding.ItemSongBinding

class PlayingNowAdapter(
    private var items: List<Song> = listOf()
) : RecyclerView.Adapter<PlayingNowAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSongBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).apply {
                root.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT}
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.song = item

        Glide.with(holder.itemView.context)
            .load(item.thumbnail)
            .into(holder.binding.imgThumbnail)
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<Song>) {
        items = newItems
        notifyDataSetChanged()
    }
}
