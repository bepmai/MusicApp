package com.mainp.musicapp.presentation.ui.viewpage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mainp.musicapp.data.entity.Song
import com.mainp.musicapp.databinding.ItemAlbumBinding

class PlayingNowAdapter(
    private var items: List<Song> = listOf()
) : RecyclerView.Adapter<PlayingNowAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAlbumBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.song = item

        Glide.with(holder.itemView.context)
            .load(item.thumbnail)
            .into(holder.binding.ivImgSong)
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<Song>) {
        items = newItems
        notifyDataSetChanged()
    }
}
