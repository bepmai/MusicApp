package com.mainp.musicapp.presentation.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mainp.musicapp.data.entity.Song
import com.mainp.musicapp.databinding.ItemAlbumBinding
import com.mainp.musicapp.databinding.ItemSongBinding

class PlayingNowAdapter(
    private var items: List<String>
) : RecyclerView.Adapter<PlayingNowAdapter.ViewHolder>() {

    class ViewHolder(var binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAlbumBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).apply {
                root.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT}
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
       Log.d("PlayingNowAdapter", "Loading image: $item")

        Glide.with(holder.itemView.context)
            .load(item)
            .into(holder.binding.ivImgSong)

    }

    override fun getItemCount(): Int = items.size

    fun updateImageList(newItems: List<String>) {
        items = newItems
        notifyDataSetChanged()
    }
}
