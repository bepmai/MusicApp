package com.mainp.musicapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mainp.musicapp.data.entity.Song
import com.mainp.musicapp.databinding.ItemSongBinding

class SongAdapter(
    private var items: List<Song> = listOf() // Khởi tạo danh sách rỗng mặc định
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
